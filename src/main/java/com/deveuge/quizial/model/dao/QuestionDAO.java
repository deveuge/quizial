package com.deveuge.quizial.model.dao;

import org.springframework.data.repository.CrudRepository;

import com.deveuge.quizial.model.entity.Question;


public interface QuestionDAO extends CrudRepository<Question, Long> {

}
