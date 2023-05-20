package com.deveuge.quizial.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.deveuge.quizial.model.dao.ResultDAO;
import com.deveuge.quizial.model.entity.Result;
import com.deveuge.quizial.model.entity.ResultId;
import com.deveuge.quizial.view.dto.ResultDTO;
import com.deveuge.quizial.view.mapper.ResultMapper;
 
@Service
public class ResultService {

	@Autowired
	private ResultDAO resultDAO;
	
	/*
	@Transactional(readOnly = true)
	public List<Result> findAll() {
		return (List<Result>) resultDAO.findAll();
	}

	@Transactional
	public void save(Result result) {
		resultDAO.save(result);
	}
	*/

	@Transactional(readOnly = true)
	public ResultDTO get(Long quizId, String letter) {
		return resultDAO.findById(new ResultId(quizId, letter))
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	/*
	@Transactional
	public void delete(ResultId id) {
		resultDAO.deleteById(id);
	}
	*/
	
	private ResultDTO mapToDTO(final Result result) {
    	return ResultMapper.INSTANCE.resultToResultDTO(result);
    }

}
