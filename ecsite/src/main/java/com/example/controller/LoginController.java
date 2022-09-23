package com.example.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.form.EndUserForm;

@Controller
public class LoginController {

	@Autowired
	private LoginCheck loginCheck;

	@GetMapping("/login")
	public String getLoginPage(Model model, Locale locale, @ModelAttribute EndUserForm form) {
		//ログイン中か判断
		String url = loginCheck.getLoginuser("login/login");
		return url;
	}

	@GetMapping("/user/login")
	public String getUserLoginPage() {
		return "redirect:/user/index";
	}

	@GetMapping("/admin/login")
	public String getAdminLoginPage() {
		//ログイン中か判断
		String url = loginCheck.getLoginuser("login/login_admin");
		return url;
	}

	@GetMapping("/admin/login/login")
	public String getAdminPage() {
		return "redirect:/admin/index";
	}
}
