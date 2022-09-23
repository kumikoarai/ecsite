package com.example.domain.service;

import java.util.List;

import com.example.entity.ProductCategory;

public interface ProductCategoryService {

	/** 商品とカテゴリの登録*/
	public void postProductCategory(List<ProductCategory> productCategory);

	/** 商品とカテゴリの取得*/
	public List<ProductCategory> getProductCategories(Integer productId);

	/** 商品とカテゴリの削除*/
	public void deleteProductCategory(Integer productId);

	/** 商品とカテゴリの取得（カテゴリ別）*/
	public List<ProductCategory> getProductCategoriesCategory(Integer categoryId);
}
