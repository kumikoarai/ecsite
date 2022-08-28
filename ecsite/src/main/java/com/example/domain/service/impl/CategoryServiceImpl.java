package com.example.domain.service.impl;

import java.util.List;

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

	@Override
	public List<Category> getCategories() {
		List<Category> Categories = repository.findAll();

		return Categories;
	}


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

}
