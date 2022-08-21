package com.example.domain.service;

import java.util.List;

import com.example.entity.Product;

public interface ProductService {

	/** 商品取得 */
	public List<Product> getProducts(Product product);
}
