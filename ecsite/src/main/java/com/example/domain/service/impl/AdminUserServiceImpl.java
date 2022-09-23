package com.example.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.service.AdminUserService;
import com.example.entity.AdminUser;
import com.example.entity.Users;
import com.example.repository.AdminUserRepository;
import com.example.repository.UsersRepository;

@Service
public class AdminUserServiceImpl implements AdminUserService {

	@Autowired
	private AdminUserRepository repository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private PasswordEncoder encoder;


	@Transactional
	@Override
	public void addAdminUser(Users user) {

		//存在チェック
		Users exists = usersRepository.findByUserEmailLike(user.getUserEmail());
		if(exists != null) {
			throw new DataAccessException("ユーザーが既に存在しています。") {};
		}

		user.setRole("ROLE_GENERAL");

		//パスワードの暗号化
		String rawPassword = user.getPassword();
		user.setPassword(encoder.encode(rawPassword));

		usersRepository.save(user);
		Users exists2 = usersRepository.findByUserEmailLike(user.getUserEmail());
		AdminUser adminUser = new AdminUser();
		adminUser.setUsersId(exists2.getUserId());
		repository.save(adminUser);

	}


	@Override
	public List<Users> getAdminUser() {

		//管理者の取得
		List<AdminUser> adminUser = repository.findAllByOrderByUserIdAsc();

		List<Integer> usersid = new ArrayList<>();
		for(AdminUser i : adminUser) {
			usersid.add(i.getUsersId());
		}

		List<Users> users = usersRepository.findAllById(usersid);

		return users;
	}


	@Override
	public Users getAdminUserOne(Integer userId) {

		// 管理者の取得（1件）
		//Optional<AdminUser> option = repository.findById(userId);

		//AdminUser user = option.orElse(null);

		Optional<Users> option2 = usersRepository.findById(userId);
		Users Users = option2.orElse(null);

		return Users;
	}


	@Transactional
	@Override
	public void updateAdminUserOne(Integer userId, String password, String userName) {

		//System.out.println(userId + " : " + password + " : " + userName);
		//パスワードの暗号化
		String encPass = encoder.encode(password);

		//System.out.println(userId + " : " + password + " : " + userName);
		// 管理者の更新（1件）
		usersRepository.updateAdminUser(userId, encPass, userName);
	}


	@Transactional
	@Override
	public void deleteAdminUserOne(Integer userId) {
		// 管理者を削除（1件）
		repository.deleteByUsersId(userId);
		usersRepository.deleteById(userId);
	};
}
