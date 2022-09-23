package com.example.entity;

import lombok.Data;

@Data
public class CartProduct {
	private Integer productId;

	private String productImage;

	private String productName;

	private Integer price;

	private Integer quantity;

	private String product_color;

	private Integer mapListKey;
}
