package com.adventureseekers.adventurewebapi.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "userDetail")
public class UserEntity {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	private UUID id;
	
	@Column(name = "username")
	@NotNull(message = "is required")
	@Size(min = 1, max = 50, message = "is required")
	@Pattern(regexp="[^ ]*", message = "cannot contain space")
	private String userName;
	
	@Column(name = "password")
	@NotNull(message = "is required")
	@Size(min = 5, message = "too short")
	@Size(max = 60, message = "too long")
	private String password;
	
	@Column(name = "email")
	@NotNull(message = "is required")
	@Email(message = "Email should be valid")
	@Size(min = 1, max = 50, message = "is required")
	private String email;
	
	@Column(name = "first_name")
	@NotNull(message = "is required")
	@Size(min = 1, max = 50, message = "is required")
	private String firstName;
	
	@Column(name = "last_name")
	@NotNull(message = "is required")
	@Size(min = 1, max = 50, message = "is required")
	private String lastName;
	
	@Column(name = "birth_date")
	@Temporal(TemporalType.DATE)
	@NotNull(message="must not be empty")
	private Date birthDate;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	@OneToOne(cascade = CascadeType.ALL,
			fetch = FetchType.LAZY)
	@JoinColumn(name = "user_detail_id")
	private UserDetailEntity userDetail;
	
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY, 
				cascade = CascadeType.ALL)
	private List<PendingEmailEntity> pendingEmail;
	
	@ManyToMany(fetch = FetchType.LAZY, 
			cascade = {
					CascadeType.DETACH,
					CascadeType.MERGE,
					CascadeType.PERSIST,
					CascadeType.REFRESH
			})
	@JoinTable(name = "users_roles", 
			joinColumns = @JoinColumn(name = "user_id"), 
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<RoleEntity> roles;

	public UserEntity(String userName, String password, String email, String firstName,
			String lastName, Date birthDate, boolean enabled, UserDetailEntity userDetail,
			Collection<RoleEntity> roles) {
		this.userName = userName.trim();
		this.password = password.trim();
		this.email = email.trim();
		this.firstName = firstName.trim();
		this.lastName = lastName.trim();
		this.birthDate = birthDate;
		this.enabled = enabled;
		this.userDetail = userDetail;
		this.roles = roles;
	}
	
	public void setUserName(String userName) {
		if (userName != null)
			this.userName = userName.trim();
		else 
			this.userName = userName;
	}
	
	public void setEmail(String email) {
		if (email != null)
			this.email = email.trim();
		else
			this.email = email;
	}
	
	public void setFirstName(String firstName) {
		if (firstName != null)
			this.firstName = firstName.trim();
		else
			this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		if (lastName != null) 
			this.lastName = lastName.trim();
		else
			this.lastName = lastName;
	}
	
	public void setPassword(String password) {
		if (password != null)
			this.password = password.trim();
		else
			this.password = password;
	}
	
}












