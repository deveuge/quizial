package com.deveuge.quizial.view.filter;

import org.springframework.data.domain.Sort.Direction;

import com.deveuge.quizial.model.entity.Quiz_;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizFilter extends BaseFilter {

	private String query;
    private Long category;
    
	public QuizFilter() {
		super(Quiz_.ID, Direction.DESC);
	}
    
}
