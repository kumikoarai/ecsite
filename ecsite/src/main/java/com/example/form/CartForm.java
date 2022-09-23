package com.example.form;

import lombok.Data;

@Data
public class CartForm {

	private Integer productId;
	private Integer quantity;
	private String product_color;
}
