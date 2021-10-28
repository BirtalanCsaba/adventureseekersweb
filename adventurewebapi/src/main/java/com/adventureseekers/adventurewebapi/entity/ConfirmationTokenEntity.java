package com.adventureseekers.adventurewebapi.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "confirmation_token")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationTokenEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "confirmed_at")
	private LocalDateTime confirmedAt;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "expired_at")
	private LocalDateTime expiredAt;
	
	@OneToOne(
			mappedBy="confirmationToken",
			cascade = {CascadeType.ALL},
			fetch = FetchType.LAZY)
	private PendingEmailEntity pendingEmail;
	
	public ConfirmationTokenEntity(String token, LocalDateTime createdAt, 
			LocalDateTime expiredAt) {
		this.token = token;
		this.createdAt = createdAt;
		this.expiredAt = expiredAt;
	}

	@Override
	public String toString() {
		return "ConfirmationToken [id=" + id + ", token=" + token + ", confirmedAt=" + confirmedAt + ", createdAt="
				+ createdAt + ", expiredAt=" + expiredAt + "]";
	}
	
}









