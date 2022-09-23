package com.example.domain.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.service.CategoryService;
import com.example.entity.Category;
import com.example.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository repository;


	/**カテゴリの取得*/
	@Override
	public List<Category> getCategories() {
		List<Category> Categories = repository.findAll();

		return Categories;
	}


	/**カテゴリの登録*/
	@Transactional
	@Override
	public void registerCategory(Category category) {
		//存在チェック
		Category exists = repository.findByCategoryNameLike(category.getCategoryName());
		if(exists != null) {
			throw new DataAccessException("このカテゴリ [" + category.getCategoryName() + "] は既に存在しています。") {};
		}
		repository.save(category);
	}


	/**カテゴリ名の取得*/
	@Override
	public String getCategoruName(Integer categoryId) {
		Optional<Category> option = repository.findById(categoryId);
		Category category = option.orElse(null);
		String categoryName = category.getCategoryName();
		return categoryName;
	}


	/**カテゴリIDの取得*/
	@Override
	public Integer getCategoryId(String categoryName) {
		Category category = repository.findByCategoryNameLike(categoryName);
		Integer categoryId = category.getCategoryId();
		return categoryId;
	}

}
