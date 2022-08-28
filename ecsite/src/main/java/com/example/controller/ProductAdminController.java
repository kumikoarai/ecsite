package com.example.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.domain.service.CategoryService;
import com.example.domain.service.ProductCategoryService;
import com.example.domain.service.ProductService;
import com.example.entity.Category;
import com.example.entity.Product;
import com.example.entity.ProductCategory;
import com.example.form.CategoryForm;
import com.example.form.ProductForm;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Controller
@RequestMapping("/admin/product")
public class ProductAdminController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ModelMapper modelMapper;


	private String remoteHost = "remoteHost";
	private String username = "username";
	private String password = "password";

	private ChannelSftp setupJsch() throws JSchException {
	    JSch jsch = new JSch();
	    jsch.setKnownHosts("/home/vpsuser/.ssh/known_hosts");

	    Session jschSession = jsch.getSession(username, remoteHost);
	    jschSession.setConfig("StrictHostKeyChecking", "no");
	    jschSession.setPassword(password);
	    jschSession.connect();
	    return (ChannelSftp) jschSession.openChannel("sftp");
	}

	public void whenUploadFileUsingJsch_thenSuccess(File newName) throws JSchException, SftpException {
	    ChannelSftp channelSftp = setupJsch();
	    channelSftp.connect();

	    String localFile = "src/main/resources/static/upload/" + newName;
	    String remoteDir = "/var/www/html/product_image/";

	    channelSftp.put(localFile, remoteDir + newName);

	    channelSftp.exit();
	}


	@GetMapping("/register")
	public String getRegistPage(Model model, Locale locale, @ModelAttribute ProductForm form) {
		List<Category> categories = categoryService.getCategories();
		model.addAttribute("categories", categories);
		//model.addAttribute("getCheckBoxItems", getCheckBoxItems());

		return "admin/product/register";
	}


	@PostMapping("/register")
	public String postRegistPage(@RequestParam("file") MultipartFile file, Model model, Locale locale, @ModelAttribute @Validated ProductForm form, BindingResult bindingResult) {
		//入力チェック結果
		if(bindingResult.hasErrors()) {
			//NG:管理者追加画面に戻ります
			return getRegistPage(model, locale, form);
		}




		File newFileName = null;
		//一旦、フォルダへ、画像の保存
		if(!file.isEmpty()) {
			try {
				// ファイル名をリネイム
				LocalDateTime nowDate = LocalDateTime.now();
				DateTimeFormatter dtf3 =
			            DateTimeFormatter.ofPattern("yyyyMMddHHmm");
			                String formatNowDate = dtf3.format(nowDate);
				File oldFileName = new File(file.getOriginalFilename());
	            newFileName = new File(formatNowDate + file.getOriginalFilename());
	            oldFileName.renameTo(newFileName);


	            // 保存先を定義
	            String uploadPath = "src/main/resources/static/upload/";
	            byte[] bytes = file.getBytes();

	         // 指定ファイルへ読み込みファイルを書き込み
	            BufferedOutputStream stream = new BufferedOutputStream(
	                    new FileOutputStream(new File(uploadPath + newFileName)));
	            stream.write(bytes);
	            stream.close();

	         // 圧縮
	            File input = new File(uploadPath + newFileName);
	            BufferedImage image = ImageIO.read(input);
	            OutputStream os = new FileOutputStream(input);
	            Iterator<ImageWriter> writers = ImageIO
	                    .getImageWritersByFormatName("jpg");
	            ImageWriter writer = (ImageWriter) writers.next();
	            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
	            writer.setOutput(ios);
	            ImageWriteParam param = new JPEGImageWriteParam(null);
	            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	            param.setCompressionQuality(0.30f);
	            writer.write(null, new IIOImage(image, null, null), param);
	            os.close();
	            ios.close();
	            writer.dispose();

			} catch (Exception e) {
				return getRegistPage(model, locale, form);
			}
		}


		try {
			//サーバーへ保存(JSchでのsftp使用)
			whenUploadFileUsingJsch_thenSuccess(newFileName);
			//一旦フォルダに保存した画像を削除
			File deletFile = new File("src/main/resources/static/upload/" + newFileName);
			deletFile.delete();
		} catch (JSchException | SftpException e) {
			System.out.println("エラー：" + e);
			e.printStackTrace();
		}



		//商品登録
		//formをCategoryに変換
		Product product = modelMapper.map(form, Product.class);
		if(form.isRelease() == true) {
			product.setRelease(false);
		} else {
			product.setRelease(true);
		}

		product.setProductImage("http://133.125.62.248/product_image/" + newFileName);

		productService.postProducts(product);

		Integer id = productService.getProductId(product.getProductImage());
		//System.out.println("ここは、商品ID：" + id);

		//System.out.println(form.getInputMultiCheck());

		List<ProductCategory> listpc = new ArrayList<ProductCategory>();

		//商品とカテゴリの連携の登録
		for(String i : form.getInputMultiCheck()) {
			Integer num = Integer.parseInt(i);
			listpc.add(new ProductCategory(null, id, num));
		}
		productCategoryService.postProductCategory(listpc);

		return "redirect:admin/product/list";

	}




	@GetMapping("/list")
	public String getListtPage(Model model, Locale locale, @ModelAttribute ProductForm form) {
		return "admin/product/list";
	}

	@GetMapping("/category")
	public String getCategoryPage(Model model, Locale locale, @ModelAttribute CategoryForm form) {
		List<Category> categories = categoryService.getCategories();

		model.addAttribute("categories", categories);
		model.addAttribute("categoriesSize", categories.size());
		return "admin/product/category";
	}

	@PostMapping("/category")
	public String postCategoryPage(Model model, Locale locale, @ModelAttribute @Validated CategoryForm form, BindingResult bindingResult) {
		//入力チェック結果
		if(bindingResult.hasErrors()) {
			//NG:管理者追加画面に戻ります
			return getCategoryPage(model, locale, form);
		}
		//formをCategoryに変換
		Category cat = modelMapper.map(form, Category.class);

		categoryService.registerCategory(cat);

		return "redirect:/admin/product/category";
	}
}
