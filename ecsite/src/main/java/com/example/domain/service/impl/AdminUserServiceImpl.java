package com.example.domain.service.impl;

import java.util.List;

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

		user.setRole("GENERAL");

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
	};
}
