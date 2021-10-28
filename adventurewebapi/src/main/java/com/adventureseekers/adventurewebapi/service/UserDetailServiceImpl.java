package com.adventureseekers.adventurewebapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adventureseekers.adventurewebapi.dao.UserDAO;
import com.adventureseekers.adventurewebapi.dao.UserDetailDAO;
import com.adventureseekers.adventurewebapi.entity.UserDetailEntity;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;

@Service
public class UserDetailServiceImpl implements UserDetailService {
	
	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private UserDetailDAO userDetailDAO;
	
	@Override
	public UserDetailEntity getByUsername(String username) {
		UserDetailEntity userDetail = this.userDao.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(username)).getUserDetail();
		return userDetail;
	}

	@Override
	public void update(UserDetailEntity theUserDetail) {
		// save or update
		this.userDetailDAO.save(theUserDetail);
	}

}
