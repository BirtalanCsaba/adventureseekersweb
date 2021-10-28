package com.adventureseekers.adventurewebapi.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Entity
@Table(name="user_detail")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "description")
	@Size(min = 1, max = 300, message = "is required")
	@With
	private String description;
	
	@Column(name = "country")
	@Size(min = 1, max = 50, message = "is required")
	@With
	private String country;
	
	@Column(name = "county")
	@Size(min = 1, max = 50, message = "is required")
	@With
	private String county;
	
	@Column(name = "city")
	@Size(min = 1, max = 50, message = "is required")
	@With
	private String city;
	
	@Lob
    @Column(name = "profile_image", columnDefinition = "mediumblob")
	private Byte[] profileImage;
	
	@OneToOne(
			mappedBy="userDetail", 
			cascade = CascadeType.ALL, 
			fetch = FetchType.LAZY)
	private UserEntity user;
	
	public UserDetailEntity(String description, String country, String county, 
			String city, Byte[] profileImage) {
		this.description = description.trim();
		this.country = country.trim();
		this.county = county.trim();
		this.city = city.trim();
		this.profileImage = profileImage;
	}
	
	public void setDescription(String description) {
		if (description != null)
			this.description = description.trim();
		else 
			this.description = description;
	}
	
	public void setCity(String city) {
		if (city != null)
			this.city = city.trim();
		else 
			this.city = city;
	}
	
	public void setCountry(String country) {
		if (country != null)
			this.country = country.trim();
		else 
			this.country = country;
	}
	
	public void setCounty(String county) {
		if (county != null)
			this.county = county.trim();
		else 
			this.county = county;
	}


}
