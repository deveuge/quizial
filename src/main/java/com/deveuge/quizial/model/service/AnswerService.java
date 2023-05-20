package com.deveuge.quizial.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deveuge.quizial.model.dao.AnswerDAO;
import com.deveuge.quizial.model.entity.Answer;
 
@Service
public class AnswerService {

	@Autowired
	private AnswerDAO answerDAO;
	
	@Transactional(readOnly=true)
	public List<Answer> findAll() {
		return (List<Answer>) answerDAO.findAll();
	}

	@Transactional
	public void save(Answer answer) {
		answerDAO.save(answer);
	}

	@Transactional(readOnly=true)
	public Answer findOne(Long id) {
		return answerDAO.findById(id).orElse(null);
	}

	@Transactional
	public void delete(Long id) {
		answerDAO.deleteById(id);
	}

}
