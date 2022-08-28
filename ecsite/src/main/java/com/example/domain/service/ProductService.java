package com.example.domain.service;

import java.util.List;

import com.example.entity.Product;

public interface ProductService {

	/** 商品取得 */
	public List<Product> getProducts(Product product);

	/** 商品登録 */
	public void postProducts(Product product);

	/** 画像パスで商品Id取得（1件） */
	public Integer getProductId(String productImg);

}
