package com.adventureseekers.adventurewebapi.service;

import java.util.Optional;

import com.adventureseekers.adventurewebapi.entity.UserDetailEntity;

public interface UserDetailService {
	
	public UserDetailEntity getByUsername(String username);

	public void update(UserDetailEntity theUserDetail);
}
