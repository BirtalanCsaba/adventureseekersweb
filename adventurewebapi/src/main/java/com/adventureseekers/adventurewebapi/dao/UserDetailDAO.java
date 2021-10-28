package com.adventureseekers.adventurewebapi.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adventureseekers.adventurewebapi.entity.UserDetailEntity;

public interface UserDetailDAO
		extends JpaRepository<UserDetailEntity, Integer> {

}
