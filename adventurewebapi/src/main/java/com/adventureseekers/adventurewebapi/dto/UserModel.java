package com.adventureseekers.adventurewebapi.dto;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

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
@JsonRootName(value = "user")
@Relation(collectionRelation = "users")
@JsonInclude(Include.NON_NULL)
public class UserModel extends RepresentationModel<UserModel> {
	
	private UUID id;
	
	@NotNull(message = "is required")
	@Size(min = 1, max = 50, message = "is required")
	@Pattern(regexp="[^ ]*", message = "cannot contain space")
	private String userName;
	
	@NotNull(message = "is required")
	@Size(min = 5, message = "too short")
	@Size(max = 60, message = "too long")
	private String password;
	
	@NotNull(message = "is required")
	@Email(message = "Email should be valid")
	@Size(min = 1, max = 50, message = "is required")
	private String email;
	
	@NotNull(message = "is required")
	@Size(min = 1, max = 50, message = "is required")
	private String firstName;
	
	@NotNull(message = "is required")
	@Size(min = 1, max = 50, message = "is required")
	private String lastName;
	
	@Temporal(TemporalType.DATE)
	@NotNull(message="must not be empty")
	private Date birthDate;
	
	private boolean enabled;
}






