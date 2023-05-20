package com.deveuge.quizial.view.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import com.deveuge.quizial.model.entity.Quiz;

public class QuizContext {
	
	@AfterMapping
	public void afterMapping(@MappingTarget Quiz quiz) {
		quiz.getQuestions().forEach(question -> {
			question.setQuiz(quiz);
			question.getAnswers().forEach(answer -> answer.setQuestion(question));
		});
		quiz.getResults().forEach(result -> result.setQuiz(quiz));
	}
}
