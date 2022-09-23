package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, Integer> {
	public void deleteByUsersId(Integer usersId);
	public List<AdminUser> findAllByOrderByUserIdAsc();
}
