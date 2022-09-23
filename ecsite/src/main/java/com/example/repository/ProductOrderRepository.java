package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
	public List<ProductOrder> findAllByOrderByOrderIdDesc();
	public List<ProductOrder> findByUserId(Integer userId);

	@Modifying
	@Query(value="select * from product_order where users_id = :userId order by order_id desc", nativeQuery = true)
	public List<ProductOrder> getProductOrderByUserId(@Param("userId") Integer userId);
}
