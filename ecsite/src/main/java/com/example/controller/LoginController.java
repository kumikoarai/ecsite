package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String getLoginPage() {
		return "login/login";
	}

	@GetMapping("/user/login")
	public String getUserLoginPage() {
		return "redirect:/user/index";
	}

	@GetMapping("/admin/login")
	public String getAdminLoginPage() {
		return "login/login_admin";
	}

	@GetMapping("/admin/login/login")
	public String getAdminPage() {
		return "redirect:/admin/index";
	}
}
