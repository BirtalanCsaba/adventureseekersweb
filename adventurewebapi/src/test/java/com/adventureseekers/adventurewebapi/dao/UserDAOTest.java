package com.adventureseekers.adventurewebapi.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.adventureseekers.adventurewebapi.entity.UserDetailEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;

@SpringBootTest
public class UserDAOTest {
	
	@Autowired
	private UserDAO userDAO;
	
	private UserEntity userEntity;
	
	@BeforeEach
    public void setUp() {
        this.userEntity = new UserEntity(
        		"user.test", 
        		"test123", 
        		"user@test.com", 
        		"firstName", 
        		"lastName", 
        		new Date(), 
        		false, 
        		new UserDetailEntity(), 
        		null);
    }
	
	@Test
	public void testSave() {
		// add a new user
		this.userDAO.save(this.userEntity);
		
		// check if the user is present
		Optional<UserEntity> theUser = 
				this.userDAO.findByUsername(this.userEntity.getUserName());
		assertTrue(theUser.isPresent());
		
		// check if the user is the same as before
		UserEntity theUserObj = theUser.get();
		assertEquals(this.userEntity.getUserName(), theUserObj.getUserName());
		assertEquals(this.userEntity.getEmail(), theUserObj.getEmail());
		assertEquals(this.userEntity.getFirstName(), theUserObj.getFirstName());
		assertEquals(this.userEntity.getLastName(), theUserObj.getLastName());
	}
	
	
	@Test
	public void testFindByUserName() {
		// add a new user
		this.userDAO.save(this.userEntity);
		
		// check if the previously added user is present with the same user name
		Optional<UserEntity> theUser =
				this.userDAO.findByUsername(this.userEntity.getUserName());
		assertTrue(theUser.isPresent());
		assertEquals(this.userEntity.getUserName(), theUser.get().getUserName());
		
		// check if returns null for an invalid user name
		theUser = this.userDAO.findByUsername("not.valid");
		assertFalse(theUser.isPresent());
	}
	
	@Test
	public void testFindByEmail() {
		// add a new user
		this.userDAO.save(this.userEntity);
		
		// check if the previously added user is present with the same email
		Optional<UserEntity> theUser =
				this.userDAO.findByEmail(this.userEntity.getEmail());
		assertTrue(theUser.isPresent());
		assertEquals(this.userEntity.getEmail(), theUser.get().getEmail());
		
		// check if returns null for an invalid user name
		theUser = this.userDAO.findByEmail("not.valid@valid.com");
		assertFalse(theUser.isPresent());
	}
	
	@Test
	public void testExistsByUsername() {
		// add a new user
		this.userDAO.save(this.userEntity);
		
		// check if the previously added user is present
		boolean isPresent = this.userDAO.existsByUsername(this.userEntity.getUserName());
		assertTrue(isPresent);
		
		// check if returns false for invalid user name
		isPresent = this.userDAO.existsByUsername("user.invalid");
		assertFalse(isPresent);
	}
	
	@Test
	public void testExistsByEmail() {
		// add a new user
		this.userDAO.save(this.userEntity);
		
		// check if the previously added user is present
		boolean isPresent = this.userDAO.existsByEmail(this.userEntity.getEmail());
		assertTrue(isPresent);
		
		isPresent = this.userDAO.existsByEmail("user.invalid@email.com");
		assertFalse(isPresent);
	}
	
	@AfterEach
    public void tearDown() {
        this.userDAO.deleteAll();
        this.userEntity = null;
    }
}









