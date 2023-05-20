package com.deveuge.quizial.model.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.util.StringUtils;

import com.deveuge.quizial.enums.Role;
import com.deveuge.quizial.util.Constants;
import com.deveuge.quizial.util.aspect.AuditableEntity;
import com.deveuge.quizial.view.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable, AuditableEntity {
	
	private static final long serialVersionUID = 3454314604460796430L;

	@Id
	@Column(nullable = false, updatable = false, length = 20)
	private String username;

	@Column(unique = true)
	private String email;

	@Column
	private boolean enabled;

	@Column
	private String image;

	@Column(length = 100)
	private String name;

	@Column
	private String password;

	@Column
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(length = 20)
	private String ip;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<Quiz> userQuizzes;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "friend", joinColumns = @JoinColumn(name = "user_username"), inverseJoinColumns = @JoinColumn(name = "user_username_friend"))
	private Set<User> friendUsers;

	@ManyToMany(mappedBy = "friendUsers")
	private Set<User> friendUsersFriend;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "favorite", joinColumns = @JoinColumn(name = "user_username"), inverseJoinColumns = @JoinColumn(name = "quiz_id"))
	private Set<Quiz> favoriteQuizzes;

	public User(String username, String name, String email, String password, String image, Role role, String ip,
			boolean enabled) {
		super();
		this.username = username;
		this.name = name;
		this.email = email;
		this.password = password;
		this.image = image;
		this.role = role;
		this.ip = ip;
		this.enabled = enabled;
	}

	public User(UserDTO user) {
		this(user.getUsername(), user.getUsername(), user.getEmail(), user.getPassword(), user.getImage(), Role.USER,
				user.getIp(), false);
	}
	
	public String getImage() {
		if(!StringUtils.hasText(image)) {
			return Constants.DEFAULT_IMAGE;
		}
		return image;
	}

	@Override
	public String getAuditableId() {
		return username;
	}

	@Override
	public User getUser() {
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled != other.enabled)
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (role != other.role)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	

}
