package com.example.domain.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.service.AdminUserService;
import com.example.entity.AdminUser;
import com.example.repository.AdminUserRepository;

@Service
public class AdminUserServiceImpl implements AdminUserService {

	@Autowired
	private AdminUserRepository repository;

	@Autowired
	private PasswordEncoder encoder;


	@Transactional
	@Override
	public void addAdminUser(AdminUser user) {

		//存在チェック
		AdminUser exists = repository.findByUserEmailLike(user.getUserEmail());
		if(exists != null) {
			throw new DataAccessException("ユーザーが既に存在しています。") {};
		}

		user.setRole("ROLE_GENERAL");

		//パスワードの暗号化
		String rawPassword = user.getPassword();
		user.setPassword(encoder.encode(rawPassword));

		repository.save(user);

	}


	@Override
	public List<AdminUser> getAdminUser() {

		//管理者の取得
		List<AdminUser> users = repository.findAllByOrderByUserIdAsc();

		return users;
	}


	@Override
	public AdminUser getAdminUserOne(Integer userId) {

		// 管理者の取得（1件）
		Optional<AdminUser> option = repository.findById(userId);

		AdminUser user = option.orElse(null);

		return user;
	}


	@Transactional
	@Override
	public void updateAdminUserOne(Integer userId, String password, String userName) {

		System.out.println(userId + " : " + password + " : " + userName);
		//パスワードの暗号化
		String encPass = encoder.encode(password);

		System.out.println(userId + " : " + password + " : " + userName);
		// 管理者の更新（1件）
		repository.updateAdminUser(userId, encPass, userName);
	}


	@Transactional
	@Override
	public void deleteAdminUserOne(Integer userId) {
		// 管理者を削除（1件）
		repository.deleteById(userId);
	};
}
