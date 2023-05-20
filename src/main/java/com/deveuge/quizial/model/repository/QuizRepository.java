package com.deveuge.quizial.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.deveuge.quizial.model.entity.Quiz;
import com.deveuge.quizial.view.filter.QuizFilter;

public interface QuizRepository {

	Page<Quiz> find(QuizFilter filter, Pageable page);
}
