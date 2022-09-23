package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.ProductMaxQuantity;

public interface ProductMaxQuantityRepository extends JpaRepository<ProductMaxQuantity, Integer> {
	public ProductMaxQuantity findByProductId(Integer productId);

	/** 商品の購入数量上限の更新*/
	@Modifying
	@Query(value="update product_max_quantity"
			+ " set"
			+ " max_quantity = CASE"
			+ " when max_quantity IS NOT NULL THEN :maxQuantity"
			+ " END"
			+ " where"
			+ " product_id = :productId", nativeQuery = true)
	public Integer updateMaxQuantity(@Param("productId") Integer productId,
			 @Param("maxQuantity") Integer maxQuantity);
}
