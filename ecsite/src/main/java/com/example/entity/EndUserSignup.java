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
@Table(name="end_user")
public class EndUserSignup {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name="user_id")
	private Integer userId;

	@Column (name="birthday")
	private Date birthday;

	@Column (name="gender")
	private Integer gender;

	@Column (name="address")
	private String address;

	@Column (name="phone_number")
	private Integer phoneNumber;


	@Column (name="users_id")
	private Integer usersId;
}
