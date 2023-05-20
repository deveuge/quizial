package com.deveuge.quizial.model.dao;

import org.springframework.data.repository.CrudRepository;

import com.deveuge.quizial.model.entity.Category;

public interface CategoryDAO extends CrudRepository<Category, Long> {

}
