package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	public List<Product> findByProductNameLike(String name);
	public Product findByProductImageLike(String image);
}
