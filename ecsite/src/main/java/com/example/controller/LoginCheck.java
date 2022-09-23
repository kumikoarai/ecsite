package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.domain.service.UserService;
import com.example.entity.Users;

@Controller
public class LoginCheck extends AuthSecurityContextHolder {

	@Autowired
	private UserService userService;

	/**ログイン中か判断*/
	public String getLoginuser(String motourl) {

		String name = getName();
		String url = null;
		if(name != "anonymousUser") {
			Users users = userService.getUsersOne(name);
			String role = users.getRole();

			if(role.equals("ROLE_ENDUSER")) {
				url = "redirect:/user/index";
			} else {
				url = "redirect:/admin/index";
			};
		} else {
			url = motourl;
		}
		return url;
	}
}
