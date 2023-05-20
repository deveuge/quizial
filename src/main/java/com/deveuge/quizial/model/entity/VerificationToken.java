package com.deveuge.quizial.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.deveuge.quizial.enums.VerificationType;
import com.deveuge.quizial.util.Constants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VerificationToken implements Serializable {
	
	private static final long serialVersionUID = 4729179926704853129L;

	@Id
	private String token;
	
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;
	
	@Column(name = "expiry_date")
	private Date expiryDate;
	
	@Enumerated(EnumType.STRING)
	private VerificationType type;

	public VerificationToken(User user, VerificationType type) {
        this.user = user;
        this.type = type;
        expiryDate = calculateExpiryDate(Constants.VERIFICATION_TOKEN_EXPIRE_MINUTES);
        token = user.getUsername() + "_" + UUID.randomUUID().toString();
    }

	private Date calculateExpiryDate(int expiryTimeInMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}
	
	public boolean isExpired() {
		return new Date().compareTo(expiryDate) > 0;
	}
	
	public String getUUIDTokenPart() {
		return token.split("_")[1];
	}
	
	public boolean isValidConfirmToken() {
		return VerificationType.CONFIRM.equals(this.getType()) && !this.isExpired();
	}
	
	public boolean isValidResetToken() {
		return VerificationType.RESET.equals(this.getType()) && !this.isExpired();
	}
	
}