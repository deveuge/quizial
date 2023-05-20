package com.deveuge.quizial.view.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.deveuge.quizial.model.entity.User;
import com.deveuge.quizial.view.dto.UserDTO;

@Mapper(uses = { QuizMapper.class })
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	UserDTO userToUserDTO(User user);
}
