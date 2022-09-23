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
@Table(name="product_max_quantity")
public class ProductMaxQuantity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name="quantity_id")
	private Integer quantityId;

	@Column (name="max_quantity")
	public Integer maxQuantity;

	@Column (name="product_id")
	private Integer productId;
}
