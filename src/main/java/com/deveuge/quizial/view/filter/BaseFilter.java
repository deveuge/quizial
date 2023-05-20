package com.deveuge.quizial.view.filter;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class BaseFilter {

	private String orderField;
	private Sort.Direction orderDirection;
	
	protected BaseFilter() {
		super();
		orderDirection = Direction.ASC;
	}
	
}
