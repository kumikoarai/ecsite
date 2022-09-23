package com.example.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.service.UserService;
import com.example.entity.EndUser;
import com.example.entity.KariUser;
import com.example.form.EndUserForm;
import com.example.form.KariUserForm;

@RestController
@RequestMapping("/user")
public class UserRestController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private HttpSession session;



	@PostMapping("/signup_edit")
	public Integer postUserKari(@Validated KariUserForm form, BindingResult bindingResult, HttpServletRequest request) {
		//formをKariUserに変換
		KariUser kariUser = modelMapper.map(form, KariUser.class);
		//仮登録
		Integer userId = userService.postUserkaritouroku(kariUser);

		//セッション再生成
	    //session.invalidate();
		session.removeAttribute("userId");
	    session = request.getSession();
	    //セッションデータ設定
	    session.setAttribute("userId", userId);
		return 0;
	}



	@PutMapping("/update")
	public Integer updateUser(EndUserForm form) {
		//formをEndUserに変換
		EndUser endUser = modelMapper.map(form, EndUser.class);

		//ユーザーを更新
		userService.updateUser(endUser);
		return 0;
	}



	@PutMapping("/updateEmail")
	public Integer updateEmail(EndUserForm form) {
		Integer usersId = form.getUsersId();
		String userEmail = form.getUserEmail();

		userService.updateUserEmail(usersId, userEmail);

		return 0;
	}
}
