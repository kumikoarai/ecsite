package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="product")
public class Product {

	@Id
	@Column (name="product_id")
	private Integer productId;

	@Column (name="product_image")
	private String productImage;

	@Column (name="product_name")
	private String productName;

	@Column (name="product_description")
	private String productDescription;

	@Column (name="price")
	private Integer price;

	@Column (name="release")
	private boolean release;
}
