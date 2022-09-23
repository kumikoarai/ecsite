package com.example.form;



import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class EndUserForm {

	private Integer userId;

	private String userName;

	private String userEmail;

	private String password;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@NotNull
	private Date birthday;

	@NotNull
	private Integer gender;

	@NotBlank
	private String address;

	@NotNull
	private Integer phoneNumber;

	private Integer usersId;
}
