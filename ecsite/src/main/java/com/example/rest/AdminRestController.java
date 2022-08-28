package com.example.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.service.AdminUserService;
import com.example.form.AdminUserForm;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

	@Autowired
	private AdminUserService adminUserService;


	/** 管理者を更新*/
	@PutMapping("/update")
	public int updateAdminUser(@Validated AdminUserForm form, BindingResult bindingResult) {

		adminUserService.updateAdminUserOne(form.getUserId(), form.getPassword(), form.getUserName());

		return 0;
	}


	/** 管理者を削除*/
	@DeleteMapping("/delete")
	public int deleteAdminUser(AdminUserForm form) {

		adminUserService.deleteAdminUserOne(form.getUserId());

		return 0;
	}
}
