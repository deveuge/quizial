package com.deveuge.quizial.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.deveuge.quizial.model.dao.QuizDAO;
import com.deveuge.quizial.model.entity.Quiz;
import com.deveuge.quizial.view.dto.QuizDTO;
import com.deveuge.quizial.view.filter.QuizFilter;
import com.deveuge.quizial.view.mapper.QuizContext;
import com.deveuge.quizial.view.mapper.QuizMapper;
 
@Service
public class QuizService {

	@Autowired
	private QuizDAO quizDAO;

	@Transactional
	public Long create(final QuizDTO quizDTO) {
		Quiz quiz = mapToEntity(quizDTO);
		quiz = quizDAO.save(quiz);
        return quiz.getId();
    }
	
	@Transactional
    public void update(final QuizDTO quizDTO) {
		quizDAO.save(mapToEntity(quizDTO));
    }
	
	@Transactional(readOnly=true)
	public Quiz findById(final Long id) {
        return quizDAO.findById(id).orElse(null);
	}
	
	@Transactional(readOnly=true)
	public QuizDTO get(final Long id) {
        return quizDAO.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

	@Transactional
	public void delete(Long id) {
		quizDAO.deleteById(id);
	}

	@Transactional(readOnly = true)
	public boolean isFavorite(Long id, String username) {
		return quizDAO.findByIdAndFavoriteUsersUsername(id, username).isPresent();
	}
	
	@Transactional(readOnly = true)
	public Page<QuizDTO> find(QuizFilter filter, Pageable page) {
	    return mapToResults(quizDAO.find(filter, page));
	}
	
	@Transactional(readOnly = true)
	public QuizDTO findNext(Long id) {
	    return mapToDTO(quizDAO.findTop1ByIdLessThanOrderByIdDesc(id));
	}

	@Transactional(readOnly = true)
	public Page<QuizDTO> findByUsername(String username, Pageable pageRequest) {
        return mapToResults(quizDAO.findAllByUserUsernameOrderByUpdatedAtDesc(username, pageRequest));
	}

	@Transactional(readOnly = true)
	public Page<QuizDTO> findFavorites(String username, Pageable pageRequest) {
        return mapToResults(quizDAO.findByFavoriteUsersUsernameIn(username, pageRequest));
	}
	
	private QuizDTO mapToDTO(final Quiz quiz) {
    	return QuizMapper.INSTANCE.quizToQuizDTO(quiz);
    }

    private Quiz mapToEntity(final QuizDTO quizDTO) {
    	return QuizMapper.INSTANCE.quizDTOToQuiz(quizDTO, new QuizContext());
    }
    
    private Page<QuizDTO> mapToResults(Page<Quiz> results) {
    	return results.map(this::mapToDTO);
    }
}
