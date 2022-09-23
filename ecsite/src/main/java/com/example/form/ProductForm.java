package com.example.form;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;

import lombok.Data;

@Data
public class ProductForm {

	private Integer productId;

	private String productImage;

	@NotBlank
	private String productName;

	private String productDescription;

	@NotNull
	@NumberFormat(pattern = "#,###")
	private Integer price;

	private boolean release;

	private List<String> categoryName;

	private String[] inputMultiCheck;

	public String[] getInputMultiCheck() {
		return inputMultiCheck;
	}

	public void setInputMultiCheck(String[] inputMultiCheck) {
		this.inputMultiCheck = inputMultiCheck;
	}

	private Integer maxQuantity;

	private List<String> colors;
}
