package com.deveuge.quizial.model.dao;

import org.springframework.data.repository.CrudRepository;

import com.deveuge.quizial.model.entity.Result;
import com.deveuge.quizial.model.entity.ResultId;


public interface ResultDAO extends CrudRepository<Result, ResultId> {
	

}
