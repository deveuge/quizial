package com.deveuge.quizial.model.dao;

import org.springframework.data.repository.CrudRepository;

import com.deveuge.quizial.model.entity.User;
import com.deveuge.quizial.model.entity.VerificationToken;

public interface VerificationTokenDAO extends CrudRepository<VerificationToken, String> {

	void deleteByUser(User user);

}
