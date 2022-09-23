package com.example.domain.service;

import com.example.entity.EndUser;
import com.example.entity.KariUser;
import com.example.entity.Users;

public interface UserService {

	/**ユーザーの仮登録*/
	public Integer postUserkaritouroku(KariUser user);

	/**ユーザーの仮登録の取得*/
	public KariUser getKariUser(Integer userId);

	/**ユーザーの登録*/
	public void postEndUser(EndUser endUser, Integer userId);

	/**ユーザー取得（1件）メールアドレスで*/
	public Users getUsersOne(String userEmail);

	/**ユーザーの更新*/
	public void updateUser(EndUser endUser);

	/**ユーザーのメールアドレスの更新*/
	public void updateUserEmail(Integer usersId, String userEmail);

	/**ユーザー取得（1件）ユーザーIDで*/
	public Users getUsersOneByUserId(Integer usersId);
}
