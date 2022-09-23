package com.example.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.service.EndUserService;
import com.example.entity.EndUserSignup;
import com.example.repository.UserRepository;

@Service
public class EndUserServiceImpl implements EndUserService {

	@Autowired
	private UserRepository userRepository;



	/** ユーザー情報（end_userテーブル）を取得*/
	@Override
	public EndUserSignup getEndUserSignupjoho(Integer usersId) {
		EndUserSignup endUserSignup = userRepository.findByUsersId(usersId);
		return endUserSignup;
	}

}
