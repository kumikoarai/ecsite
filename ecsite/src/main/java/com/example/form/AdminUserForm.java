package com.example.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class AdminUserForm {

	private Integer userId;

	@NotBlank
	private String userName;

	@NotBlank
	@Email
	private String userEmail;

	@NotBlank
	@Length(min =4, max=100)
	@Pattern(regexp = "^[a-zA-Z0-9]+$")
	private String password;
}
