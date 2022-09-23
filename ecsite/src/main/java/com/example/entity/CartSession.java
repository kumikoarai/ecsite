package com.example.entity;

import lombok.Data;

@Data
public class CartSession {

	private Integer productId;
	private Integer quantity;
	private String product_color;
}
