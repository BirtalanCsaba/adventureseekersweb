package com.adventureseekers.adventurewebapi.model.assembler;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.adventureseekers.adventurewebapi.dto.UserDetailModel;
import com.adventureseekers.adventurewebapi.entity.UserDetailEntity;
import com.adventureseekers.adventurewebapi.rest.UserRestController;

@Component
public class UserDetailModelAssembler
		extends RepresentationModelAssemblerSupport<UserDetailEntity, UserDetailModel> {

	public UserDetailModelAssembler() {
		super(UserRestController.class, UserDetailModel.class);
	}

	@Override
	public UserDetailModel toModel(UserDetailEntity entity) {
		UserDetailModel userDetailModel = instantiateModel(entity);
		
		Link selfLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserRestController.class)
				.getUserDetailsByUsername(entity.getUser().getUserName()))
				.withSelfRel();
		Link additionalLink = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserRestController.class)
				.getUserByUsername(entity.getUser().getUserName()))
				.withSelfRel();
		
		userDetailModel.setDescription(entity.getDescription());
		userDetailModel.setCity(entity.getCity());
		userDetailModel.setCountry(entity.getCountry());
		userDetailModel.setCounty(entity.getCounty());
		userDetailModel.setProfileImage(entity.getProfileImage());
		
		userDetailModel.add(selfLink, additionalLink);
		
		return userDetailModel;
	}

}









