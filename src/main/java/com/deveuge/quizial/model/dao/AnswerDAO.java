package com.deveuge.quizial.model.dao;

import org.springframework.data.repository.CrudRepository;

import com.deveuge.quizial.model.entity.Answer;


public interface AnswerDAO extends CrudRepository<Answer, Long> {

}
