package com.adventureseekers.adventurewebapi.dto;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "detail")
@JsonInclude(Include.NON_NULL)
public class UserDetailModel extends RepresentationModel<UserDetailModel> {
	
	private Integer id;
	private String description;
	private String country;
	private String county;
	private String city;
	private Byte[] profileImage;
}
