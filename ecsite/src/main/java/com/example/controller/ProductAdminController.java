package com.example.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.domain.service.CategoryService;
import com.example.domain.service.EndUserService;
import com.example.domain.service.OrderService;
import com.example.domain.service.ProductCategoryService;
import com.example.domain.service.ProductOptionService;
import com.example.domain.service.ProductService;
import com.example.domain.service.UserService;
import com.example.entity.Category;
import com.example.entity.EndUser;
import com.example.entity.EndUserSignup;
import com.example.entity.OrderDetail;
import com.example.entity.OrderProductDetail;
import com.example.entity.OrderProducts;
import com.example.entity.Product;
import com.example.entity.ProductCategory;
import com.example.entity.ProductMaxQuantity;
import com.example.entity.ProductOptionColor;
import com.example.entity.ProductOrder;
import com.example.entity.Users;
import com.example.form.CategoryForm;
import com.example.form.ProductForm;
import com.example.form.TopProductListForm;
import com.jcraft.jsch.JSchException;
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

	@Autowired
	private ProductOptionService productOptionService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private EndUserService endUserService;



	@GetMapping("/register")
	public String getRegistPage(Model model, Locale locale, @ModelAttribute ProductForm form) {
		List<Category> categories = categoryService.getCategories();
		model.addAttribute("categories", categories);

		return "admin/product/register";
	}


	@PostMapping("/register")
	public String postRegistPage(@RequestParam("file") MultipartFile file, Model model, Locale locale, @ModelAttribute @Validated ProductForm form, BindingResult bindingResult) {
		//入力チェック結果
		if(bindingResult.hasErrors()) {
			//NG:管理者追加画面に戻ります
			return getRegistPage(model, locale, form);
		}

		//商品登録
		//formをCategoryに変換
		Product product = modelMapper.map(form, Product.class);
		if(form.isRelease() == true) {
			product.setRelease(false);
		} else {
			product.setRelease(true);
		}

		/*=================================クライアントPCまたはtomcatのwebappsから、サーバーのアプリ外部フォルダへの画像の保存処理↓ここから===============================================*/
		File newFileName = null;
		//一旦、フォルダへ、画像の保存
		if(!file.isEmpty()) {
			System.out.println("画像ある");
			newFileName = productService.postProductImageFolder(file);

			try {
				//サーバーへ保存(JSchでのsftp使用)
				productService.whenUploadFileUsingJsch_thenSuccess(newFileName);
				//一旦フォルダに保存した画像を削除
				File deletFile = new File("/opt/apache-tomcat-9.0.65/webapps/ecsite/WEB-INF/classes/static/upload/" + newFileName);
				deletFile.delete();
			} catch (JSchException | SftpException e) {
				System.out.println("エラー：" + e);
				e.printStackTrace();
			}
			product.setProductImage("https://ecsite-sample-kumiko.net/product_image/" + newFileName);
		} else {
			//System.out.println("画像なし");
			product.setProductImage("なし");
		}
		/*=================================クライアントPCまたはtomcatのwebappsから、サーバーのアプリ外部フォルダへの画像の保存処理↑ここまで===============================================*/
		/*==============================================tomcatのwebapps下への画像の保存処理↓ここから===============================================*/
		/*File newFileName = null;
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
				String uploadPath = "/opt/apache-tomcat-9.0.65/webapps/ecsite/WEB-INF/classes/static/upload/";
				//String uploadPath = "/var/www/html/product_image/";
				byte[] bytes = file.getBytes();

				// 指定ファイルへ読み込みファイルを書き込み
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File(uploadPath + newFileName)));
				stream.write(bytes);
				stream.close();

				product.setProductImage("/upload/" + newFileName.getName());
			} catch (Exception e) {
				product.setProductImage("なし");
			}
		} else {
			product.setProductImage("なし");
		}*/
		/*==============================================tomcatのwebapps下への画像の保存処理↑ここまで===============================================*/

		Integer id = productService.postProducts(product);

		List<ProductCategory> listpc = new ArrayList<ProductCategory>();

		//商品とカテゴリの連携の登録
		for(String i : form.getInputMultiCheck()) {
			Integer num = Integer.parseInt(i);
			listpc.add(new ProductCategory(null, id, num));
		}
		productCategoryService.postProductCategory(listpc);

		//formをProductMaxQuantityに変換
		if(form.getMaxQuantity() == null) {
			form.setMaxQuantity(10);
		}
		ProductMaxQuantity productMaxQuantity = modelMapper.map(form, ProductMaxQuantity.class);
		productMaxQuantity.setProductId(id);

		//Listに追加
		List<ProductOptionColor> productOptionColors = new ArrayList<ProductOptionColor> ();


		for(String color : form.getColors()) {
			ProductOptionColor productOptionColor = new ProductOptionColor();
			productOptionColor.setColor(color);
			productOptionColor.setProductId(id);

			productOptionColors.add(productOptionColor);
		}

		productOptionService.postMaxQuantityAndColor(productMaxQuantity, productOptionColors);

		return "redirect:/admin/product/list";

	}



	@PostMapping("/test/register")
	public String postTestRegistPage(@RequestParam("file") MultipartFile file, Model model, Locale locale, @ModelAttribute @Validated ProductForm form, BindingResult bindingResult) {
		return "redirect:/admin/product/list";
	}



	@PostMapping("/detail")
	public String updateProduct(@RequestParam("file") MultipartFile file, Model model, Locale locale, @ModelAttribute @Validated ProductForm form, BindingResult bindingResult) {
		//System.out.println("更新のためのコントロールです");

		//入力チェック結果
		if(bindingResult.hasErrors()) {
			//NG:管理者追加画面に戻ります
			return getRegistPage(model, locale, form);
		}

		//もし空じゃなければ、一旦、フォルダへ、画像の保存
		if(!file.isEmpty()) {
			File newFileName = null;
			/*==============================================クライアントPCからサーバーへの画像の保存処理↓ここから===============================================*/
			newFileName = productService.postProductImageFolder(file);

			try {
				//サーバーへ保存(JSchでのsftp使用)
				productService.whenUploadFileUsingJsch_thenSuccess(newFileName);
				if(!form.getProductImage().isEmpty()) {
					String oldImage = form.getProductImage();
					//String fileName = new File(oldImage).getName();
					//古い画像のサーバーからの削除
					productService.whenDeleteFileUsingJsch_thenSuccess(oldImage);
				}
				//一旦フォルダに保存した画像を削除
				File deletFile = new File("/opt/apache-tomcat-9.0.65/webapps/ecsite/WEB-INF/classes/static/upload/" + newFileName);
				deletFile.delete();

			} catch (JSchException | SftpException e) {
				System.out.println("エラー：" + e);
				e.printStackTrace();
			}
			form.setProductImage("https://ecsite-sample-kumiko.net/product_image/" + newFileName.getName());

			/*==============================================クライアントPCからサーバーへの画像の保存処理↑ここまで===============================================*/
			/*==============================================本番環境でサーバーへの画像の保存処理↓ここから===============================================*/

			/*try {
				// ファイル名をリネイム
				LocalDateTime nowDate = LocalDateTime.now();
				DateTimeFormatter dtf3 =
			            DateTimeFormatter.ofPattern("yyyyMMddHHmm");
			                String formatNowDate = dtf3.format(nowDate);
				File oldFileName = new File(file.getOriginalFilename());
				newFileName = new File(formatNowDate + file.getOriginalFilename());
				oldFileName.renameTo(newFileName);

				// 保存先を定義
				String uploadPath = "/opt/apache-tomcat-9.0.65/webapps/ecsite/WEB-INF/classes/static/upload/";
				byte[] bytes = file.getBytes();

				// 指定ファイルへ読み込みファイルを書き込み
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File(uploadPath + newFileName)));
				stream.write(bytes);
				stream.close();

				String[] bits = form.getProductImage().split("/");
				String lastOne = bits[bits.length-1];
				productService.deleteProductImage(lastOne);

				form.setProductImage("/upload/" + newFileName.getName());

			} catch (Exception e) {
				form.setProductImage("なし");
			}*/
			/*==============================================本番環境でサーバーへの画像の保存処理↑ここまで===============================================*/

		}

		//商品とカテゴリの連携の登録
		List<ProductCategory> listpc = new ArrayList<ProductCategory>();
		for(String i : form.getInputMultiCheck()) {
			Integer num = Integer.parseInt(i);
			listpc.add(new ProductCategory(null, form.getProductId(), num));
		}
		productCategoryService.deleteProductCategory(form.getProductId());
		productCategoryService.postProductCategory(listpc);

		//商品を更新
		productService.updateProduct(form.getProductId(), form.getProductImage(), form.getProductName(), form.getProductDescription(), form.getPrice(), form.isRelease());

		//商品の購入数量上限と色の更新
		//formをProductMaxQuantityに変換
		if(form.getMaxQuantity() == null) {
			form.setMaxQuantity(10);
		}
		ProductMaxQuantity productMaxQuantity = new ProductMaxQuantity();
		//ProductMaxQuantity productMaxQuantity = modelMapper.map(form, ProductMaxQuantity.class);
		productMaxQuantity.setMaxQuantity(form.getMaxQuantity());
		productMaxQuantity.setProductId(form.getProductId());

		//Listに追加
		List<ProductOptionColor> productOptionColors = new ArrayList<ProductOptionColor> ();

		if(form.getColors() != null) {
			for(String color : form.getColors()) {
				ProductOptionColor productOptionColor = new ProductOptionColor();
				productOptionColor.setColor(color);
				productOptionColor.setProductId(form.getProductId());

				productOptionColors.add(productOptionColor);
			}
		}
		productOptionService.updateMaxQuantityAndColor(productMaxQuantity, productOptionColors);
		return "redirect:/admin/product/list";
	}



	@GetMapping("/list")
	public String getListtPage(@ModelAttribute TopProductListForm form, Model model) {
		//formをProductに変換
		Product product = modelMapper.map(form, Product.class);

		//商品一覧取得
		List<Product> productList = productService.getProductsAdmin(product);

		model.addAttribute("productList", productList);

		return "admin/product/list";
	}

	@GetMapping("/detail/{productId}")
	public String getProductDetailPage(Model model, ProductForm form, @PathVariable("productId") Integer productId) {

		/* 商品取得（1件） */
		Product product = productService.getProductsOne(productId);

		/* 商品のカテゴリ取得*/
		List<ProductCategory> pc = productCategoryService.getProductCategories(productId);

		//System.out.println(pc);

		/* カテゴリ取得 */
		List<Category> categories = categoryService.getCategories();
		model.addAttribute("categories", categories);

		List<String> ctname = new ArrayList<String>();
		if(pc != null) {
			for(ProductCategory pci : pc) {
				for(Category ct : categories) {
					if(pci.getCategoryId() == ct.getCategoryId()) {
						ctname.add(new String(ct.getCategoryName()));
					}
				}
			}
		} else {
			ctname.add(null);
		}

		//System.out.println("ctname：" + ctname);
		product.setCategoryName(ctname);

		//Productをformに変換
		form = modelMapper.map(product, ProductForm.class);


		//商品の購入数量上限の取得
		Integer productMaxQuantity = productOptionService.getProductMaxQuantity(productId);
		//商品の色の取得
		List<String> productOptionColors = productOptionService.getProductOptionColor(productId);
		form.setMaxQuantity(productMaxQuantity);
		form.setColors(productOptionColors);

		//モデルに登録
		model.addAttribute("productForm", form);

		return "admin/product/detail_edit";
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



	@GetMapping("/order")
	public String getOrderList(Model model) {
		//オーダーの取得
		List<ProductOrder> productOrderList = orderService.getProductOrder();
		List<OrderProducts> productOrderList2 = new ArrayList<> ();
		for(ProductOrder productOrderOne : productOrderList) {
			//ユーザー取得（1件）ユーザーIDで
			Users users = userService.getUsersOneByUserId(productOrderOne.getUserId());
			OrderProducts orderProducts = new OrderProducts();
			orderProducts.setUserName(users.getUserName());
			orderProducts.setOrderId(productOrderOne.getOrderId());
			orderProducts.setOrderDate(productOrderOne.getOrderDate());
			orderProducts.setTotalPrice(productOrderOne.getTotalPrice());
			orderProducts.setUserId(productOrderOne.getUserId());
			productOrderList2.add(orderProducts);
		}
		model.addAttribute("productOrderList", productOrderList2);
		return "admin/product/order_list";
	}



	@GetMapping("/order/detail/{orderId}")
	public String getOrderDetailPage(Model model, @PathVariable("orderId") Integer orderId) {
		//オーダーの詳細の取得
		List<OrderDetail> orderDetailList = orderService.getOrderDetailByOrderId(orderId);

		List<OrderProductDetail> orderProductDetailList = new ArrayList<> ();

		for(OrderDetail orderDetail : orderDetailList) {
			OrderProductDetail orderProductDetail = new OrderProductDetail();
			//商品取得(1件)
			Product product = productService.getProductsOne(orderDetail.getProductId());
			//情報をセット
			orderProductDetail.setProductId(orderDetail.getProductId());
			orderProductDetail.setProductImage(product.getProductImage());
			orderProductDetail.setProductName(product.getProductName());
			orderProductDetail.setPrice(product.getPrice());
			orderProductDetail.setOrderDetailId(orderDetail.getOrderDetailId());
			orderProductDetail.setQuantity(orderDetail.getQuantity());
			orderProductDetail.setProductColor(orderDetail.getProductColor());
			//リストに追加
			orderProductDetailList.add(orderProductDetail);
		}

		//オーダーの取得
		ProductOrder productOrder = orderService.getProductOrderOneByOrderId(orderId);

		//ユーザー取得（1件）ユーザーIDで
		Users users = userService.getUsersOneByUserId(productOrder.getUserId());

		//ユーザー情報（end_userテーブル）を取得
		EndUserSignup endUserjoho = endUserService.getEndUserSignupjoho(productOrder.getUserId());

		EndUser endUser = new EndUser();
		endUser.setUserId(endUserjoho.getUserId());
		endUser.setUsersId(productOrder.getUserId());
		endUser.setUserName(users.getUserName());
		endUser.setUserEmail(users.getUserEmail());
		endUser.setAddress(endUserjoho.getAddress());
		endUser.setBirthday(endUserjoho.getBirthday());
		endUser.setGender(endUserjoho.getGender());
		endUser.setPhoneNumber(endUserjoho.getPhoneNumber());

		model.addAttribute("endUser", endUser);
		model.addAttribute("orderProductDetailList", orderProductDetailList);
		model.addAttribute("productOrder", productOrder);
		return "admin/product/order_detail";
	}
}
