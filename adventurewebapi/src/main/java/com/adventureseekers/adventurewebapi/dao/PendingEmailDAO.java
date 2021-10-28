package com.adventureseekers.adventurewebapi.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adventureseekers.adventurewebapi.entity.PendingEmailEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;

public interface PendingEmailDAO
	extends JpaRepository<PendingEmailEntity, UserEntity> {
	
	Optional<PendingEmailEntity> findByEmail(String email);
	
	@Query("SELECT p FROM PendingEmailEntity p "
			+ "WHERE p.user=:userEntity AND p.confirmationToken.confirmedAt=null")
	Optional<PendingEmailEntity> findPendingEmail(@Param("userEntity")UserEntity userEntity);
}
