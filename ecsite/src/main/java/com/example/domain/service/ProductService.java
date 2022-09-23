package com.example.domain.service;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Product;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public interface ProductService {

	/** 商品取得 エンドユーザー */
	public List<Product> getProducts(Product product);

	/** 商品取得 管理者用 */
	public List<Product> getProductsAdmin(Product product);

	/** 商品取得（1件） */
	public Product getProductsOne(Integer productId);

	/** 商品登録 */
	public Integer postProducts(Product product);

	/** 画像パスで商品Id取得（1件） */
	public Integer getProductId(String productImg);

	/** 商品を更新*/
	public void updateProduct(Integer userId, String productImage, String productName, String productDescription, Integer price, boolean release);

	/** 商品の画像のサーバーへの保存 */
	public void whenUploadFileUsingJsch_thenSuccess(File newName)throws JSchException, SftpException;

	/** 商品の画像のサーバーからの削除 */
	public void whenDeleteFileUsingJsch_thenSuccess(String newName) throws JSchException, SftpException;

	/** 商品の画像のフォルダへの保存 */
	public File postProductImageFolder(MultipartFile file);

	/** 商品の削除 */
	public void deleteProduct(Integer productId);

}
