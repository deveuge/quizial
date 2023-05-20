package com.deveuge.quizial.view.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.deveuge.quizial.model.entity.Quiz;
import com.deveuge.quizial.view.dto.QuizDTO;

@Mapper(uses = {QuestionMapper.class, AnswerMapper.class, ResultMapper.class, CategoryMapper.class})
public interface QuizMapper {

	QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);
	
	@Mapping(source = "user.username", target = "user")
	@Mapping(source = "user.image", target = "userImage")
	@Mapping(source = "category.id", target = "category")
    QuizDTO quizToQuizDTO(Quiz quiz);
	@Mapping(expression = "java(null)", target = "user")
	@Mapping(source = "category", target = "category.id")
    Quiz quizDTOToQuiz(QuizDTO quizDTO, @Context QuizContext context);
}
