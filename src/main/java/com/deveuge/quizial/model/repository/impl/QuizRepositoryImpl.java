package com.deveuge.quizial.model.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.deveuge.quizial.model.entity.Category_;
import com.deveuge.quizial.model.entity.Quiz;
import com.deveuge.quizial.model.entity.Quiz_;
import com.deveuge.quizial.model.entity.User_;
import com.deveuge.quizial.model.repository.BaseRepository;
import com.deveuge.quizial.model.repository.QuizRepository;
import com.deveuge.quizial.view.filter.BaseFilter;
import com.deveuge.quizial.view.filter.QuizFilter;

@Repository
public class QuizRepositoryImpl extends BaseRepository<Quiz> implements QuizRepository {

	public QuizRepositoryImpl(JpaContext context) {
		super(Quiz.class, context);
	}

	@Override
	@Transactional
	public Page<Quiz> find(QuizFilter filter, Pageable page) {
		Session session = this.em.unwrap(Session.class);
		return find(session, filter, page);
	}
	
	@Override
	protected void setRootJoins(Root<Quiz> root) {
		root.join(Quiz_.USER);
		root.join(Quiz_.CATEGORY);
	}

	@Override
	protected List<Predicate> getFilterConditions(CriteriaBuilder builder, Root<Quiz> root, BaseFilter baseFilter) {
		QuizFilter filter = (QuizFilter) baseFilter;
		List<Predicate> predicates = new ArrayList<>();
		
		if(!StringUtils.isEmpty(filter.getQuery())) {
			predicates.add(
				builder.or(
					builder.like(root.get(Quiz_.NAME), "%" + filter.getQuery() + "%"),
					builder.like(root.get(Quiz_.DESCRIPTION), "%" + filter.getQuery() + "%"),
					builder.like(root.get(Quiz_.USER).get(User_.USERNAME), "%" + filter.getQuery() + "%"),
					builder.like(root.get(Quiz_.USER).get(User_.NAME), "%" + filter.getQuery() + "%")
				)
			);
		}
		if(filter.getCategory() != null) {
			predicates.add(builder.equal(root.get(Quiz_.CATEGORY).get(Category_.ID), filter.getCategory()));
		}
		
		return predicates;
	}
}
