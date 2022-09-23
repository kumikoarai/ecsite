package com.example.domain.service;

import com.example.entity.EndUserSignup;

public interface EndUserService {

	/** ユーザー情報（end_userテーブル）を取得*/
	public EndUserSignup getEndUserSignupjoho(Integer usersId);
}
