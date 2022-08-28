package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, Integer> {

	public AdminUser findByUserEmailLike(String email);
	public List<AdminUser> findAllByOrderByUserIdAsc();

	/** 管理者更新*/
	@Modifying
	@Query(value="update admin_user"
			+ " set"
			+ " password = CASE"
			+ " when password IS NOT NULL THEN :password"
			+ " when password IS NULL THEN password"
			+ " END"
			+ " , user_name = CASE"
			+ " when user_name IS NOT NULL THEN :userName"
			+ " when user_name IS NULL THEN user_name"
			+ " END"
			+ " where"
			+ " user_id = :userId", nativeQuery = true)
	public Integer updateAdminUser(@Param("userId") Integer userId,
			 @Param("password") String password,
			 @Param("userName") String userName);
}
