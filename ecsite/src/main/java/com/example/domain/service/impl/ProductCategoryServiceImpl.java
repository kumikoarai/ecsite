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

	@Transactional
	@Override
	public void postProductCategory(List<ProductCategory> productCategory) {
		/** 商品とカテゴリの登録*/
		System.out.println("ここはインプル：" + productCategory);
		repository.saveAll(productCategory);
		/*for(ProductCategory i : productCategory) {
			System.out.println("ここはインプル：" + i);
			repository.save(i);
		}*/

	};
}
