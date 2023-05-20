package com.deveuge.quizial.view.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class QuizFormDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long[] answersId;
	
	private Long nextQuizId;
	private String nextQuizTitle;

	public QuizFormDTO(int questionsSize, QuizDTO quizDTO) {
		super();
		this.answersId = new Long[questionsSize];
		if(quizDTO != null) {
			this.nextQuizId = quizDTO.getId();
			this.nextQuizTitle = quizDTO.getName();
		}
	}
	
}
