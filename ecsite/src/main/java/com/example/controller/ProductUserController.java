package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.service.ProductOptionService;
import com.example.domain.service.ProductService;
import com.example.entity.Product;
import com.example.form.CartForm;

@Controller
@RequestMapping("/user/product")
public class ProductUserController {

	@Autowired
	private ProductService productService;

	@Autowired
	private Categories categories;

	@Autowired
	private UserController userController;

	@Autowired
	private ProductOptionService productOptionService;


	@GetMapping("/detail/{productId}")
	public String getProductDetailPage(@PathVariable("productId") Integer productId, Model model, @ModelAttribute CartForm form) {

		/* 商品取得（1件） */
		Product product = productService.getProductsOne(productId);

		//商品の購入数量上限の取得
		Integer maxQuantity = productOptionService.getProductMaxQuantity(productId);
		if(maxQuantity == 0) {
			maxQuantity = 10;
		}
		List<Integer> quantity = new ArrayList<Integer> ();
		for(Integer i=0; i<maxQuantity; i++) {
			quantity.add(i + 1);
		}

		//商品の色の取得
		List<String> colors = productOptionService.getProductOptionColor(productId);
		Integer noColors = null;
		if(colors.size() == 0) {
			noColors = 0;
		}

		//モデルに登録
		model.addAttribute("product", product);
		categories.getCategoryAll(model);
		model.addAttribute("quantity", quantity);
		model.addAttribute("colors", colors);
		model.addAttribute("noColors", noColors);
		model.addAttribute("productId", productId);

		//ログイン中のユーザー名をModelに追加
		userController.setModelUserName(model);

		return "user/product/detail";
	}



}
