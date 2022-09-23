package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	public List<Product> findByProductNameLikeAndReleaseIs(String name, boolean release);
	public List<Product> findByProductNameLike(String name);
	public Product findByProductImageLike(String image);
	public List<Product> findAllByOrderByProductIdAsc();

	/** 商品の更新*/
	@Modifying
	@Query(value="update product"
			+ " set"
			+ " product_image = CASE"
			+ " when product_image IS NOT NULL THEN :productImage"
			+ " when product_image IS NULL THEN product_image"
			+ " END"
			+ " , product_name = CASE"
			+ " when product_name IS NOT NULL THEN :productName"
			+ " when product_name IS NULL THEN product_name"
			+ " END"
			+ " , product_description = CASE"
			+ " when product_description IS NOT NULL THEN :productDescription"
			+ " when product_description IS NULL THEN product_description"
			+ " END"
			+ " , price = CASE"
			+ " when price IS NOT NULL THEN :price"
			+ " when price IS NULL THEN price"
			+ " END"
			+ " , release = CASE"
			+ " when release IS NOT NULL THEN :release"
			+ " when release IS NULL THEN release"
			+ " END"
			+ " where"
			+ " product_id = :productId", nativeQuery = true)
	public Integer updateProduct(@Param("productId") Integer userId,
			 @Param("productImage") String productImage,
			 @Param("productName") String productName,
			 @Param("productDescription") String productDescription,
			 @Param("price") Integer price,
			 @Param("release") boolean release);
}
