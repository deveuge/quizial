package com.deveuge.quizial.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QuizSearchType {
	USER(0, "quizzes-search"),
    POPULAR(1, "quizzes-popular"),
    NEW(2, "quizzes-new"),
    FRIENDS(3, "quizzes-friends");
    
    private int number;
    private String tabName;
}