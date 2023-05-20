package com.deveuge.quizial.model.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.deveuge.quizial.model.entity.Quiz;
import com.deveuge.quizial.model.repository.QuizRepository;

public interface QuizDAO extends CrudRepository<Quiz, Long>, QuizRepository {
	
	Page<Quiz> findAllByUserUsernameOrderByUpdatedAtDesc(String username, Pageable pageRequest);
	Page<Quiz> findByFavoriteUsersUsernameIn(String username, Pageable pageRequest);
	Optional<Quiz> findByIdAndFavoriteUsersUsername(Long id, String username);
	Quiz findTop1ByIdLessThanOrderByIdDesc(Long id);
}
