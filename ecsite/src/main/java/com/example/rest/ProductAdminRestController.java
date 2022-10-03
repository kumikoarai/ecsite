package com.example.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.service.ProductCategoryService;
import com.example.domain.service.ProductService;
import com.example.form.ProductForm;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

@RestController
@RequestMapping("/admin/product")
public class ProductAdminRestController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductCategoryService productCategoryService;

	/** 商品を削除*/
	@DeleteMapping("/delete")
	public int deleteProduct(ProductForm form) {
		String oldImage = form.getProductImage();
		//String fileName = new File(oldImage).getName();
		if (oldImage.equals("なし")) {

		} else {
			//productService.deleteProductImage(oldImage);
			try {
				productService.whenDeleteFileUsingJsch_thenSuccess(oldImage);
			} catch (JSchException | SftpException e) {
				System.out.println("エラー：" + e);
				e.printStackTrace();
			}

		}

		productCategoryService.deleteProductCategory(form.getProductId());

		productService.deleteProduct(form.getProductId());

		return 0;
	}
}
