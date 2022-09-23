package com.example.entity;

import java.util.Date;

import lombok.Data;

@Data
public class OrderProducts {

	private Integer orderId;

	private Integer userId;

	private Date orderDate;

	private Integer totalPrice;

	private String userName;

}
