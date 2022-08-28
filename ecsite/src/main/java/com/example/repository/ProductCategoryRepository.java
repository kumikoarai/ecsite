package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

}
