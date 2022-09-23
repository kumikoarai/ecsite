package com.example.entity;

import java.util.Date;

import lombok.Data;

@Data
public class EndUser {

	private Integer userId;

	private String userName;

	private String userEmail;

	private String password;

	private String role;

	private Date birthday;

	private Integer gender;

	private String address;

	private Integer phoneNumber;

	private Integer usersId;
}
