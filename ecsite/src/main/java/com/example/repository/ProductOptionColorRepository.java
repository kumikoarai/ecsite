package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.ProductOptionColor;

public interface ProductOptionColorRepository extends JpaRepository<ProductOptionColor, Integer> {
	public  List<ProductOptionColor> findByProductId(Integer productId);
	public void deleteByProductId(Integer productId);
}
