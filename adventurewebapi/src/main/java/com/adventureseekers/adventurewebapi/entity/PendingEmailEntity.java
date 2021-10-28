package com.adventureseekers.adventurewebapi.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="pending_email")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ToString(exclude = "userDetail")
public class PendingEmailEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@ManyToOne(
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, 
					CascadeType.DETACH, CascadeType.REFRESH},
			fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private UserEntity user;
	
	@OneToOne(
			cascade = {CascadeType.ALL},
			fetch = FetchType.LAZY)
	@JoinColumn(name = "confirmation_token_id")
	@JsonIgnore
	private ConfirmationTokenEntity confirmationToken;
	
	@Column(name = "email")
	@NotNull(message = "is required")
	@Email(message = "Email should be valid")
	@Size(min = 1, max = 50, message = "is required")
	private String email;

	public PendingEmailEntity(String email) {
		this.email = email.trim();
	}
	
	public void setEmail(String email) {
		if (email != null)
			this.email = email.trim();
		else
			this.email = email;
	}
	
}



















