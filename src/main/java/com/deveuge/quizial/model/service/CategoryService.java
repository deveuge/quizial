package com.deveuge.quizial.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deveuge.quizial.model.dao.CategoryDAO;
import com.deveuge.quizial.model.entity.Category;
import com.deveuge.quizial.view.dto.CategoryDTO;
import com.deveuge.quizial.view.mapper.CategoryMapper;
 
@Service
public class CategoryService {

	@Autowired
	private CategoryDAO categoryDAO;
	
	@Transactional(readOnly=true)
	public List<CategoryDTO> findAll() {
		return ((List<Category>) categoryDAO.findAll()).stream()
				.map(this::mapToDTO)
				.collect(Collectors.toList());
	}
	
	private CategoryDTO mapToDTO(final Category category) {
    	return CategoryMapper.INSTANCE.categoryToCategoryDTO(category);
    }

    private Category mapToEntity(final CategoryDTO categoryDTO) {
    	return CategoryMapper.INSTANCE.categoryDTOToCategory(categoryDTO);
    }
}
