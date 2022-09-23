package com.example.domain.service;

import java.util.List;

import com.example.entity.Category;

public interface CategoryService {

	/**カテゴリの取得*/
	public List<Category> getCategories ();

	/**カテゴリの登録*/
	public void registerCategory(Category category);

	/**カテゴリ名の取得*/
	public String getCategoruName(Integer categoryId);

	/**カテゴリIDの取得*/
	public Integer getCategoryId(String categoryName);
}
