package com.deveuge.quizial.model.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.deveuge.quizial.model.entity.User;

public interface UserDAO extends CrudRepository<User, String> {
	
	User findByEmail(String email);
	User findByUsernameOrEmail(String username, String email);
	Page<User> findByFriendUsersFriendUsername(String username, Pageable pageRequest);
	Optional<User> findByUsernameAndFriendUsersUsername(String username, String friendUsersUsername);
	int countByIp(String ip);

}
