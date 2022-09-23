package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
	public List<ProductCategory> findByProductId(Integer productId);
	public List<ProductCategory> findByCategoryId(Integer categoryId);
	public void deleteByProductId(Integer productId);
}
