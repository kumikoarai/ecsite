package com.example.rest;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.service.AdminUserService;
import com.example.domain.service.ProductService;
import com.example.entity.Product;
import com.example.form.AdminUserForm;
import com.example.form.TopProductListForm;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

	@Autowired
	private AdminUserService adminUserService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ModelMapper modelMapper;

	/** 商品を検索*/
	@GetMapping("/product/search")
	public List<Product> getProductsList(TopProductListForm form) {

		//formをProductに変換
		Product product = modelMapper.map(form, Product.class);

		//商品一覧取得
		List<Product> productList = productService.getProductsAdmin(product);

		return productList;
	}

	/** 管理者を更新*/
	@PutMapping("/update")
	public int updateAdminUser(@Validated AdminUserForm form, BindingResult bindingResult) {

		adminUserService.updateAdminUserOne(form.getUserId(), form.getPassword(), form.getUserName());

		return 0;
	}


	/** 管理者を削除*/
	@DeleteMapping("/delete")
	public int deleteAdminUser(AdminUserForm form) {

		adminUserService.deleteAdminUserOne(form.getUserId());

		return 0;
	}


}
