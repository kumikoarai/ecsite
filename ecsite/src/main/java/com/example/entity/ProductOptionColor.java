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
@Table(name="product_option_color")
public class ProductOptionColor {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name="color_id")
	private Integer colorId;

	@Column (name="color")
	public String color;

	@Column (name="product_id")
	private Integer productId;
}
