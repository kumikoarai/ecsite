package com.example.domain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.service.ProductService;
import com.example.entity.Product;
import com.example.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository repository;

	/** 商品取得 */
	@Override
	public List<Product> getProducts(Product product) {
		if(product.getProductName() == null) {
			return repository.findAll();
		} else {
			//検索条件
			List<Product> proList = repository.findByProductNameLike("%" + product.getProductName() + "%");
			return proList;
		}
	}

	@Transactional
	@Override
	public void postProducts(Product product) {
		repository.save(product);

	}

	@Override
	public Integer getProductId(String productImg) {
		Product product = repository.findByProductImageLike(productImg);
		return product.getProductId();
	}
}

