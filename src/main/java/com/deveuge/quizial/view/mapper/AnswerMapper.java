package com.deveuge.quizial.view.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.deveuge.quizial.model.entity.Answer;
import com.deveuge.quizial.view.dto.AnswerDTO;

@Mapper(uses = { ResultMapper.class })
public interface AnswerMapper {

	AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

	AnswerDTO answerToAnswerDTO(Answer answer);
	Answer answerDTOToAnswer(AnswerDTO answerDTO);
}
