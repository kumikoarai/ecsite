package com.example.domain.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.domain.service.ProductService;
import com.example.entity.Product;
import com.example.repository.ProductRepository;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository repository;

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



	/** 商品取得 エンドユーザー */
	@Override
	public List<Product> getProducts(Product product) {
		if(product.getProductName() == null) {
			return repository.findAllByOrderByProductIdAsc();
		} else {
			//検索条件
			List<Product> proList = repository.findByProductNameLikeAndReleaseIs("%" + product.getProductName() + "%", true);
			return proList;
		}
	}

	/** 商品取得 管理者用 */
	@Override
	public List<Product> getProductsAdmin(Product product) {
		if(product.getProductName() == null) {
			return repository.findAllByOrderByProductIdAsc();
		} else {
			//検索条件
			List<Product> proList = repository.findByProductNameLike("%" + product.getProductName() + "%");
			return proList;
		}
	}

	/** 商品取得（1件） */
	@Override
	public Product getProductsOne(Integer productId) {
		Optional<Product> option = repository.findById(productId);
		Product product = option.orElse(null);
		return product;
	}

	/** 商品登録 */
	@Transactional
	@Override
	public Integer postProducts(Product product) {
		Product product2 = repository.saveAndFlush(product);
		return product2.getProductId();
	}

	/** 画像パスで商品Id取得（1件） */
	@Override
	public Integer getProductId(String productImg) {
		Product product = repository.findByProductImageLike(productImg);
		return product.getProductId();
	}


	/** 商品を更新*/
	@Transactional
	@Override
	public void updateProduct(Integer userId, String productImage, String productName, String productDescription, Integer price, boolean release) {
		repository.updateProduct(userId, productImage, productName, productDescription, price, release);

	}

	/** 商品の画像のサーバーへの保存 */
	@Transactional
	@Override
	public void whenUploadFileUsingJsch_thenSuccess(File newName) throws JSchException, SftpException {
	    ChannelSftp channelSftp = setupJsch();
	    channelSftp.connect();

	    String localFile = "/opt/apache-tomcat-9.0.65/webapps/ecsite/WEB-INF/classes/static/upload/" + newName;
	    String remoteDir = "/var/www/html/product_image/";

	    channelSftp.put(localFile, remoteDir + newName);

	    channelSftp.exit();
	}

	/** 商品の画像のサーバーからの削除 */
	@Transactional
	@Override
	public void whenDeleteFileUsingJsch_thenSuccess(String fileName) throws JSchException, SftpException {
	    ChannelSftp channelSftp = setupJsch();
	    channelSftp.connect();

	    String[] bits = fileName.split("/");
		String lastOne = bits[bits.length-1];
	    channelSftp.rm("/var/www/html/product_image/" + lastOne);

	    channelSftp.exit();
	}


	/** 商品の画像のフォルダへの保存 */
	@Transactional
	@Override
	public File postProductImageFolder(MultipartFile file) {
		File newFileName = null;
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
            //String uploadPath = "src/main/resources/static/upload/";←クライアントPC時のパス
            //↓tomcatへwarファイルで置いた時のパス
            String uploadPath = "/opt/apache-tomcat-9.0.65/webapps/ecsite/WEB-INF/classes/static/upload/";
            byte[] bytes = file.getBytes();

         // 指定ファイルへ読み込みファイルを書き込み
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(new File(uploadPath + newFileName)));
            stream.write(bytes);
            stream.close();

		} catch (Exception e) {

		}
		return newFileName;
	}



	/** 本番環境で商品画像のサーバーからの削除 */
	@Transactional
	@Override
	public void deleteProductImage(String fileName) {
		if(fileName.equals("なし")) {
			String[] bits = fileName.split("/");
			String lastOne = bits[bits.length-1];
			Path p = Paths.get("/var/www/html/product_image/" + lastOne);

			try{
			  Files.delete(p);
			}catch(IOException e){
			  System.out.println(e);
			}

		} else {

		}
	};



	/** 商品の削除 */
	@Transactional
	@Override
	public void deleteProduct(Integer productId) {
		repository.deleteById(productId);

	}


}

