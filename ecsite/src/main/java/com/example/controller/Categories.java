package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.domain.service.CategoryService;
import com.example.entity.Category;

@Controller
public class Categories {

	@Autowired
	private CategoryService categoryService;

	/**商品カテゴリを全て取得*/
	public void getCategoryAll(Model model) {
		//カテゴリを全て取得
		List<Category> categories = categoryService.getCategories();
		model.addAttribute("categories", categories);
	}
}
