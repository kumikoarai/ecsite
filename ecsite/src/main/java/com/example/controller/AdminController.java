package com.example.controller;

import java.util.List;
import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.service.AdminUserService;
import com.example.entity.Users;
import com.example.form.AdminUserForm;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminUserService adminUserService;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/index")
	public String getAdminPage() {
		return "admin/index";
	}

	@GetMapping("/admin")
	public String getAdminAdminPage(Model model) {
		List<Users> adminUser = adminUserService.getAdminUser();
		Integer adminUserSize = adminUser.size();

		if(adminUserSize >= 4) {
			model.addAttribute("sizeText", "※管理者登録人数が制限に達しています。追加するには管理者を削除してください。（一般管理者は3人まで）");
		}

		model.addAttribute("adminUser", adminUser);
		return "admin/admin";
	}



	@GetMapping("/add")
	public String getAdminAddPage(Model model, Locale locale, @ModelAttribute AdminUserForm form) {
		return "admin/add";
	}


	@PostMapping("/add")
	public String postAdminAdd(Model model, Locale locale, @ModelAttribute @Validated AdminUserForm form, BindingResult bindingResult) {

		//入力チェック結果
		if(bindingResult.hasErrors()) {
			//NG:管理者追加画面に戻ります
			return getAdminAddPage(model, locale, form);
		}

		//formをAdminUserに変換
		Users adminUser = modelMapper.map(form, Users.class);

		//管理者追加登録
		adminUserService.addAdminUser(adminUser);

		return "redirect:/admin/admin";
	}


	@GetMapping("/edit/{userId}")
	public String getAdminEdit(Model model, AdminUserForm form, @PathVariable("userId") Integer userId) {

		/* 管理者の取得（1件）*/
		Users user = adminUserService.getAdminUserOne(userId);
		user.setPassword(null);

		//AdminUserをformに変換
		form = modelMapper.map(user, AdminUserForm.class);

		form.setUserId(userId);

		//モデルに登録
		model.addAttribute("adminUserForm", form);
		model.addAttribute("adminUserRole", user.getRole());

		return "admin/edit";
	}

}
