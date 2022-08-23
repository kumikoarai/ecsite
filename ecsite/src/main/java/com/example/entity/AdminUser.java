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
@Table(name="admin_user")
public class AdminUser {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column (name="user_id")
	private Integer userId;

	@Column (name="user_name")
	private String userName;

	@Column (name="user_email")
	private String userEmail;

	@Column (name="password")
	private String password;

	@Column (name="role")
	private String role;
}
