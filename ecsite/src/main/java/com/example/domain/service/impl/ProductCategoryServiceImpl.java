package com.example.domain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.service.ProductCategoryService;
import com.example.entity.ProductCategory;
import com.example.repository.ProductCategoryRepository;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

	@Autowired
	private ProductCategoryRepository repository;

	/** 商品とカテゴリの登録*/
	@Transactional
	@Override
	public void postProductCategory(List<ProductCategory> productCategory) {
		System.out.println("ここはインプル：" + productCategory);
		repository.saveAll(productCategory);
	}

	/** 商品とカテゴリの取得*/
	@Override
	public List<ProductCategory> getProductCategories(Integer productId) {
		List<ProductCategory> productCategories = repository.findByProductId(productId);
		return productCategories;
	}



	/** 商品とカテゴリの削除*/
	@Transactional
	@Override
	public void deleteProductCategory(Integer productId) {
		repository.deleteByProductId(productId);
	}

	/** 商品とカテゴリの取得（カテゴリ別）*/
	@Override
	public List<ProductCategory> getProductCategoriesCategory(Integer categoryId) {
		List<ProductCategory> productCategories = repository.findByCategoryId(categoryId);
		return productCategories;
	};
}
