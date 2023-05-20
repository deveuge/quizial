package com.deveuge.quizial.view.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.deveuge.quizial.enums.Role;
import com.deveuge.quizial.util.SecurityUtils;
import com.deveuge.quizial.view.validator.AccountNumber;
import com.deveuge.quizial.view.validator.EqualFields;
import com.deveuge.quizial.view.validator.UniqueEmail;
import com.deveuge.quizial.view.validator.UniqueLogin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualFields(baseField = "password", matchField = "repeatPassword")
public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@UniqueLogin
	@Size(min = 3, max = 15)
	private String username;
	@Size(min = 3, max = 25)
	private String name;
	@UniqueEmail
	@Email
	@Size(min = 3, max = 90)
	private String email;
	@Size(min = 8, max = 20)
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Pattern.user.password")
	private String password;
	private String image;
	private Role role;
	private String repeatPassword;
	private String resetEmail;
	@AccountNumber
	private String ip;
	
	public UserDTO() {
		super();
		this.ip = SecurityUtils.getIp();
	}
	
}
