package com.example.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CategoryForm {

	private Integer categoryId;

	@NotBlank
	private String categoryName;

}
