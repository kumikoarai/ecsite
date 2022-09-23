package com.example.domain.service;

import java.util.List;

import com.example.entity.Users;

public interface AdminUserService {

	/** 管理者の追加*/
	public void addAdminUser(Users user);

	/** 管理者の取得*/
	public List<Users> getAdminUser();

	/** 管理者の取得（1件）*/
	public Users getAdminUserOne(Integer userId);

	/** 管理者の更新（1件）*/
	public void updateAdminUserOne(Integer userId, String password, String userName);

	/** 管理者を削除（1件）*/
	public void deleteAdminUserOne(Integer userId);
}
