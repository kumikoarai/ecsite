package com.example.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.EndUserSignup;

public interface UserRepository extends JpaRepository<EndUserSignup, Integer> {
	public EndUserSignup findByUsersId(Integer usersId);



	/** エンドユーザーの更新*/
	@Modifying
	@Query(value="update end_user"
			+ " set"
			+ " birthday = CASE"
			+ " when birthday IS NOT NULL THEN :birthday"
			+ " when birthday IS NULL THEN birthday"
			+ " END"
			+ " , address = CASE"
			+ " when address IS NOT NULL THEN :address"
			+ " when address IS NULL THEN address"
			+ " END"
			+ " , phone_number = CASE"
			+ " when phone_number IS NOT NULL THEN :phoneNumber"
			+ " when phone_number IS NULL THEN phone_number"
			+ " END"
			+ " where"
			+ " user_id = :userId", nativeQuery = true)
	public Integer updateEndUser(@Param("userId") Integer userId,
			 @Param("birthday") Date birthday,
			 @Param("address") String address,
			 @Param("phoneNumber") Integer phoneNumber);
}
