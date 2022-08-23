package com.example.domain.service;

import java.util.List;

import com.example.entity.AdminUser;

public interface AdminUserService {

	/** 管理者の追加*/
	public void addAdminUser(AdminUser user);

	/** 管理者の取得*/
	public List<AdminUser> getAdminUser();

}
