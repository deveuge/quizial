package com.deveuge.quizial.view.filter;

import org.springframework.data.domain.Sort.Direction;

import com.deveuge.quizial.model.entity.Quiz_;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizFilter extends BaseFilter {

	private String query;
    private Long category;
    private String userFriends;
    
	public QuizFilter() {
		super(Quiz_.ID, Direction.DESC);
	}

	@Builder
	public QuizFilter(String query, Long category, String userFriends, String orderField, Direction orderDirection) {
		super(orderField, orderDirection);
		this.query = query;
		this.category = category;
		this.userFriends = userFriends;
	}
	
}
