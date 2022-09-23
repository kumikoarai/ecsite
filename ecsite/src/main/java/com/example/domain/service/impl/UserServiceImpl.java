package com.example.domain.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.service.UserService;
import com.example.entity.EndUser;
import com.example.entity.EndUserSignup;
import com.example.entity.KariUser;
import com.example.entity.Users;
import com.example.repository.KariUserRepository;
import com.example.repository.UserRepository;
import com.example.repository.UsersRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private KariUserRepository kariUserRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private PasswordEncoder encoder;


	/**ユーザーの仮登録*/
	@Transactional
	@Override
	public Integer postUserkaritouroku(KariUser user) {

		//存在チェック
		KariUser exists = kariUserRepository.findByUserEmailLike(user.getUserEmail());
		Users exists2 = usersRepository.findByUserEmailLike(user.getUserEmail());
		if(exists != null || exists2 != null) {
			throw new DataAccessException("ユーザーが既に存在しています。") {};
		}

		//パスワードの暗号化
		String rawPassword = user.getPassword();
		user.setPassword(encoder.encode(rawPassword));

		kariUserRepository.save(user);
		KariUser thisuser = kariUserRepository.findByUserEmailLike(user.getUserEmail());
		return thisuser.getUserId();
	}

	/**ユーザーの仮登録の取得*/
	@Override
	public KariUser getKariUser(Integer userId) {
		Optional<KariUser> option = kariUserRepository.findById(userId);
		KariUser kariUser = option.orElse(null);
		return kariUser;
	}

	/**ユーザーの登録*/
	@Transactional
	@Override
	public void postEndUser(EndUser endUser, Integer userId) {
		EndUserSignup endUserSignup = new EndUserSignup();
		endUser.setRole("ROLE_ENDUSER");
		Users users = new Users();
		users.setUserEmail(endUser.getUserEmail());
		users.setUserName(endUser.getUserName());
		users.setPassword(endUser.getPassword());
		users.setRole("ROLE_ENDUSER");
		usersRepository.save(users);
		Users exists = usersRepository.findByUserEmailLike(users.getUserEmail());
		endUser.setUsersId(exists.getUserId());
		endUserSignup.setAddress(endUser.getAddress());
		endUserSignup.setBirthday(endUser.getBirthday());
		endUserSignup.setGender(endUser.getGender());
		endUserSignup.setPhoneNumber(endUser.getPhoneNumber());
		endUserSignup.setUsersId(endUser.getUsersId());
		userRepository.save(endUserSignup);

		kariUserRepository.deleteById(userId);
	}

	/**ユーザー取得（1件）*/
	@Override
	public Users getUsersOne(String userEmail) {
		Users users = usersRepository.findByUserEmailLike(userEmail);
		return users;
	}



	/**ユーザーの更新*/
	@Transactional
	@Override
	public void updateUser(EndUser endUser) {
		// TODO 自動生成されたメソッド・スタブ
		Integer usersId = endUser.getUsersId();
		String userName = endUser.getUserName();
		String userEmail = endUser.getUserEmail();

		//パスワードの暗号化
		String rawPassword = endUser.getPassword();
		endUser.setPassword(encoder.encode(rawPassword));

		String password = endUser.getPassword();

		Integer userId = endUser.getUserId();
		Date birthday = endUser.getBirthday();
		String address = endUser.getAddress();
		Integer phoneNumber = endUser.getPhoneNumber();

		usersRepository.updateEndUser(usersId, password, userName, userEmail);
		userRepository.updateEndUser(userId, birthday, address, phoneNumber);
	}



	/**ユーザーのメールアドレスの更新*/
	@Transactional
	@Override
	public void updateUserEmail(Integer usersId, String userEmail) {
		usersRepository.updateEndUserEmail(usersId, userEmail);

	}



	/**ユーザー取得（1件）ユーザーIDで*/
	@Override
	public Users getUsersOneByUserId(Integer usersId) {
		Optional<Users> option = usersRepository.findById(usersId);
		Users users = option.orElse(null);
		return users;
	}

}
