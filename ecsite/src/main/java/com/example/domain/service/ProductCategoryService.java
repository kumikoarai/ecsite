package com.example.domain.service;

import java.util.List;

import com.example.entity.ProductCategory;

public interface ProductCategoryService {

	/** 商品とカテゴリの登録*/
	public void postProductCategory(List<ProductCategory> productCategory);
}
