package com.example.rest;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.service.ProductService;
import com.example.entity.Product;
import com.example.form.TopProductListForm;

@RestController
public class TopRestController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ModelMapper modelMapper;

	/** 商品を検索*/
	@GetMapping("/top/search")
	public List<Product> getProductsList(TopProductListForm form) {

		//formをProductに変換
		Product product = modelMapper.map(form, Product.class);

		//商品一覧取得
		List<Product> productList = productService.getProducts(product);

		return productList;
	}
}
