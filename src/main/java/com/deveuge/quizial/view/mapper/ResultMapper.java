package com.deveuge.quizial.view.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.deveuge.quizial.model.entity.Result;
import com.deveuge.quizial.view.dto.ResultDTO;

@Mapper
public interface ResultMapper {

ResultMapper INSTANCE = Mappers.getMapper(ResultMapper.class);
	
	@Mapping(source = "id.quizId", target = "id")
	@Mapping(source = "id.letter", target = "resultLetter")
    ResultDTO resultToResultDTO(Result result);
	@Mapping(source = "id", target = "id.quizId")
	@Mapping(source = "resultLetter", target = "id.letter")
    Result resultDTOToResult(ResultDTO resultDTO);
}
