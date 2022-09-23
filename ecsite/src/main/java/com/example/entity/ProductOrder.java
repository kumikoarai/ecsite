package com.example.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="product_order")
public class ProductOrder {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name="order_id")
	private Integer orderId;

	@Column (name="users_id")
	private Integer userId;

	@Column (name="order_date")
	private Date orderDate;

	@Column (name="total_price")
	private Integer totalPrice;

/*
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProductOrder) {
			ProductOrder condition = (ProductOrder) obj;
				return this.orderId == (condition.orderId) && this.userId == (condition.userId) &&
						this.orderDate.equals(condition.orderDate) && this.totalPrice == (condition.totalPrice);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderId, userId, orderDate, totalPrice);
	}*/
}
