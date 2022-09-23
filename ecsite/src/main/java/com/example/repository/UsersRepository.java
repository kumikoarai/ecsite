package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {
	public Users findByUserEmailLike(String email);


	/** 管理者更新*/
	@Modifying
	@Query(value="update users"
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



	/** エンドユーザーの更新*/
	@Modifying
	@Query(value="update users"
			+ " set"
			+ " password = CASE"
			+ " when password IS NOT NULL THEN :password"
			+ " when password IS NULL THEN password"
			+ " END"
			+ " , user_name = CASE"
			+ " when user_name IS NOT NULL THEN :userName"
			+ " when user_name IS NULL THEN user_name"
			+ " END"
			+ " , user_email = CASE"
			+ " when user_email IS NOT NULL THEN :userEmail"
			+ " when user_email IS NULL THEN user_email"
			+ " END"
			+ " where"
			+ " user_id = :userId", nativeQuery = true)
	public Integer updateEndUser(@Param("userId") Integer userId,
			 @Param("password") String password,
			 @Param("userName") String userName,
			 @Param("userEmail") String userEmail);


	/**ユーザーのメールアドレスの更新*/
	@Modifying
	@Query(value="update users"
			+ " set"
			+ " user_email = CASE"
			+ " when user_email IS NOT NULL THEN :userEmail"
			+ " when user_email IS NULL THEN user_email"
			+ " END"
			+ " where"
			+ " user_id = :userId", nativeQuery = true)
	public Integer updateEndUserEmail(@Param("userId") Integer userId,
			 @Param("userEmail") String userEmail);
}
