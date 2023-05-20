package com.deveuge.quizial.view.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.deveuge.quizial.model.entity.Question;
import com.deveuge.quizial.view.dto.QuestionDTO;

@Mapper(uses = {AnswerMapper.class})
public interface QuestionMapper {
	
	QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);
	
    QuestionDTO questionToQuestionDTO(Question question);
    Question questionDTOToQuestion(QuestionDTO questionDTO);

}
