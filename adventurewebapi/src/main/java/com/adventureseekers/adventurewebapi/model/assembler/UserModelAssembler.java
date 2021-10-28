package com.adventureseekers.adventurewebapi.model.assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.adventureseekers.adventurewebapi.dto.UserModel;
import com.adventureseekers.adventurewebapi.entity.UserEntity;
import com.adventureseekers.adventurewebapi.rest.UserRestController;

import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler 
			extends RepresentationModelAssemblerSupport<UserEntity, UserModel> {

	public UserModelAssembler() {
		super(UserRestController.class, UserModel.class);
	}

	@Override
	public UserModel toModel(UserEntity entity) {
		UserModel userModel = instantiateModel(entity);
		
		Link selfLink = linkTo(methodOn(UserRestController.class)
				.getUserByUsername(entity.getUserName()))
				.withSelfRel();
		
		Link additionalLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserRestController.class)
				.getUserDetailsByUsername(entity.getUserName()))
				.withRel("details")
				.withType("GET");
		
		
		userModel.add(selfLink, additionalLink);
		
		userModel.setUserName(entity.getUserName());
		userModel.setEmail(entity.getEmail());
		userModel.setFirstName(entity.getFirstName());
		userModel.setLastName(entity.getLastName());
		userModel.setBirthDate(entity.getBirthDate());
		userModel.setEnabled(entity.isEnabled());
		
		return userModel;
	}
	
}
