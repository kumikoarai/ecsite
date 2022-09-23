package com.example.entity;

import lombok.Data;

@Data
public class OrderProductDetail {
	private Integer productId;

	private String productImage;

	private String productName;

	private Integer price;

	private Integer orderDetailId;

	private Integer quantity;

	private String productColor;
}
