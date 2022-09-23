package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="order_detail")
public class OrderDetail {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name="id")
	private Integer orderDetailId;

	@Column (name="order_id")
	private Integer orderId;

	@Column (name="product_id")
	private Integer productId;

	@Column (name="quantity")
	private Integer quantity;

	@Column (name="product_color")
	private String productColor;
}
