package com.adventureseekers.adventurewebapi.rest;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adventureseekers.adventurewebapi.dto.UserDetailModel;
import com.adventureseekers.adventurewebapi.dto.UserModel;
import com.adventureseekers.adventurewebapi.entity.UserDetailEntity;
import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.exception.UserNotFoundException;
import com.adventureseekers.adventurewebapi.helpers.EmailHelper;
import com.adventureseekers.adventurewebapi.model.assembler.UserDetailModelAssembler;
import com.adventureseekers.adventurewebapi.model.assembler.UserModelAssembler;
import com.adventureseekers.adventurewebapi.response.StringResponse;
import com.adventureseekers.adventurewebapi.service.PendingEmailService;
import com.adventureseekers.adventurewebapi.service.UserDetailService;
import com.adventureseekers.adventurewebapi.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
	
	private Logger logger = LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDetailService userDetailService;
	
	@Autowired
	private PendingEmailService pendingEmailService;
	
	@Autowired
	private EmailHelper emailHelper;
	
	@Autowired
	private UserModelAssembler userModelAssembler;
	
	@Autowired
	private UserDetailModelAssembler userDetailModelAssembler;
	
	@GetMapping("/checkEmail")
	public Map<String, Boolean> checkEmailExists(@RequestParam("email") String email) {
		Boolean emailFound = this.userService.existsByEmail(email);
		return Collections.singletonMap("success", emailFound);
	}
	
	@GetMapping("/checkUsername")
	public Map<String, Boolean> checkUsernameExists(@RequestParam("username") String username) {
		Boolean usernameFound = this.userService.existsByUsername(username);
		return Collections.singletonMap("success", usernameFound);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<UserModel> getUserByUsername(
			@PathVariable("username") String username) {
		return this.userService.findByUserName(username)
				.map(this.userModelAssembler::toModel)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/details/{username}")
	public ResponseEntity<UserDetailModel> getUserDetailsByUsername(
			@PathVariable("username") String username) {
		UserDetailEntity userDetailEntity = this.userDetailService.getByUsername(username);
		return ResponseEntity.ok(this.userDetailModelAssembler.toModel(userDetailEntity));
	}

	/*@GetMapping("/details/profileImage/{username}")
	public ResponseEntity<?> getProfileImage(@PathVariable String username) {

	}*/

	@DeleteMapping("/{username}")
	public ResponseEntity<StringResponse> deleteUserByUsername(
			@PathVariable("username") String username) {
		this.userService.delete(username);
		return ResponseEntity.ok(new StringResponse("User " + username + " deleted"));
	}
	
	@PatchMapping(path = "/{username}")
	public ResponseEntity<StringResponse> patchUser(
			@PathVariable String username, 
			@Valid @RequestBody UserModel newUser, 
			BindingResult bindingResult) 
					throws MethodArgumentNotValidException, NoSuchMethodException, SecurityException {
		
		// The transmitted parameter errors
		BindException actualErrors = new BindException(newUser, "user");
		
		UserEntity theUser = this.userService.findByUserName(username)
				.orElseThrow(() -> new UserNotFoundException(username));
		
		// need to update the data
		boolean needUpdate = false;
		
		if (StringUtils.hasLength(newUser.getUserName())) {
			// validation
			if (bindingResult.hasFieldErrors("userName")) {
				actualErrors.addError(Objects.requireNonNull(bindingResult.getFieldError("userName")));
			}
			else {
				if (!Objects.equals(theUser.getUserName(), newUser.getUserName())) {
					theUser.setUserName(newUser.getUserName());
					needUpdate = true;
				}
			}
		}
		
		if (StringUtils.hasLength(newUser.getEmail())) {
			if (bindingResult.hasFieldErrors("email")) {
				actualErrors.addError(Objects.requireNonNull(bindingResult.getFieldError("email")));
			}
			else {
				// create a pending email for confirmation
				this.pendingEmailService.create(theUser, newUser.getEmail());
			}
		}
		
		if (StringUtils.hasLength(newUser.getFirstName())) {
			if (bindingResult.hasFieldErrors("firstName")) {
				actualErrors.addError(Objects.requireNonNull(bindingResult.getFieldError("firstName")));
			}
			else {
				if (!Objects.equals(theUser.getFirstName(), newUser.getFirstName())) {
					theUser.setFirstName(newUser.getFirstName());
					needUpdate = true;			
				}
			}
		}
		
		if (StringUtils.hasLength(newUser.getLastName())) {
			if (bindingResult.hasFieldErrors("lastName")) {
				actualErrors.addError(Objects.requireNonNull(bindingResult.getFieldError("lastName")));
			}
			else {
				if (!Objects.equals(theUser.getLastName(), newUser.getLastName())) {
					theUser.setLastName(newUser.getLastName());
					needUpdate = true;
				}
			}
		}
		
		if (newUser.getBirthDate() != null) {
			if (bindingResult.hasFieldErrors("birthDate")) {
				actualErrors.addError(Objects.requireNonNull(bindingResult.getFieldError("birthDate")));
			}
			else {
				if (!Objects.equals(theUser.getBirthDate(), newUser.getBirthDate())) {
					theUser.setBirthDate(newUser.getBirthDate());
					needUpdate = true;
				}
			}
		}
		
		if (actualErrors.hasErrors()) {
			MethodParameter parameter = new MethodParameter(
					this.getClass().getMethod("patchUser", String.class, UserModel.class, BindingResult.class), 0);
			throw new MethodArgumentNotValidException(parameter, actualErrors);
		}
		
		if (needUpdate) {
	        this.userService.update(theUser);
	        return ResponseEntity.ok(new StringResponse("User updated successfuly"));
	    }
		
		return ResponseEntity.ok(new StringResponse("User does not need to be updated"));
	}
	
	@PatchMapping(path = "/details/{username}")
	public ResponseEntity<StringResponse> patchUserDetails(
			@PathVariable String username, 
			@Valid @RequestBody UserDetailModel newUserDetail, 
			BindingResult bindingResult) 
					throws MethodArgumentNotValidException, NoSuchMethodException, SecurityException {
		
		// The transmitted parameter errors
		BindException actualErrors = new BindException(newUserDetail, "userDetail");
		
		UserDetailEntity theUserDetail = this.userDetailService.getByUsername(username);
		
		// need to update the data
		boolean needUpdate = false;
		
		if (StringUtils.hasLength(newUserDetail.getDescription())) {
			// validation
			if (bindingResult.hasFieldErrors("description")) {
				actualErrors.addError(Objects.requireNonNull(bindingResult.getFieldError("description")));
			}
			else {
				if (!Objects.equals(theUserDetail.getDescription(), newUserDetail.getDescription())) {
					theUserDetail.setDescription(newUserDetail.getDescription());
					needUpdate = true;
				}
			}
		}
		
		if (StringUtils.hasLength(newUserDetail.getCity())) {
			// validation
			if (bindingResult.hasFieldErrors("city")) {
				actualErrors.addError(Objects.requireNonNull(bindingResult.getFieldError("city")));
			}
			else {
				if (!Objects.equals(theUserDetail.getCity(), newUserDetail.getCity())) {
					theUserDetail.setCity(newUserDetail.getCity());
					needUpdate = true;
				}
			}
		}
		
		if (StringUtils.hasLength(newUserDetail.getCountry())) {
			// validation
			if (bindingResult.hasFieldErrors("country")) {
				actualErrors.addError(Objects.requireNonNull(bindingResult.getFieldError("country")));
			}
			else {
				if (!Objects.equals(theUserDetail.getCountry(), newUserDetail.getCountry())) {
					theUserDetail.setCountry(newUserDetail.getCountry());
					needUpdate = true;
				}
			}
		}
		
		if (StringUtils.hasLength(newUserDetail.getCounty())) {
			// validation
			if (bindingResult.hasFieldErrors("city")) {
				actualErrors.addError(Objects.requireNonNull(bindingResult.getFieldError("city")));
			}
			else {
				if (!Objects.equals(theUserDetail.getCounty(), newUserDetail.getCounty())) {
					theUserDetail.setCounty(newUserDetail.getCounty());
					needUpdate = true;
				}
			}
		}
		
		if (newUserDetail.getProfileImage() != null) {
			// validation
			if (bindingResult.hasFieldErrors("profileImage")) {
				actualErrors.addError(Objects.requireNonNull(bindingResult.getFieldError("profileImage")));
			}
			else {
				theUserDetail.setProfileImage(newUserDetail.getProfileImage());
				needUpdate = true;
			}
		}
		
		if (actualErrors.hasErrors()) {
			MethodParameter parameter = new MethodParameter(
					this.getClass().getMethod("patchUserDetails", String.class, UserDetailModel.class, BindingResult.class), 0);
			throw new MethodArgumentNotValidException(parameter, actualErrors);
		}
		
		if (needUpdate) {
	        this.userDetailService.update(theUserDetail);
	        return ResponseEntity.ok(new StringResponse("User updated successfuly"));
	    }
		
		return ResponseEntity.ok(new StringResponse("User does not need to be updated"));
	}
	
}









