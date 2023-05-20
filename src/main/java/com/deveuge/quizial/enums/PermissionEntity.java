package com.deveuge.quizial.enums;

import com.deveuge.quizial.model.entity.Quiz;
import com.deveuge.quizial.model.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PermissionEntity {
	QUIZ(Quiz.class),
	USER(User.class);
	
	private Class<?> clazz;
}
