package com.example.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.service.ProductService;
import com.example.entity.Product;
import com.example.form.TopProductListForm;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/index")
	public String getUserPage(@ModelAttribute TopProductListForm form, Model model) {

		//formをProductに変換
		Product product = modelMapper.map(form, Product.class);

		//商品一覧取得
		List<Product> productList = productService.getProducts(product);

		model.addAttribute("productList", productList);
		return "user/index";
	}

}
