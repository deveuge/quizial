package com.deveuge.quizial.view.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.deveuge.quizial.model.entity.Category;
import com.deveuge.quizial.view.dto.CategoryDTO;

@Mapper
public interface CategoryMapper {

	CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

	CategoryDTO categoryToCategoryDTO(Category category);
	Category categoryDTOToCategory(CategoryDTO categoryDTO);
}
