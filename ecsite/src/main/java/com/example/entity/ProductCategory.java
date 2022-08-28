package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="p_category")
public class ProductCategory {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name="id")
	private Integer id;

	@Column (name="product_id")
	private Integer productId;

	@Column (name="category_id")
	private Integer categoryId;
}
