package com.deveuge.quizial.model.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.repository.support.PageableExecutionUtils;

import com.deveuge.quizial.model.entity.User;
import com.deveuge.quizial.view.filter.BaseFilter;

public abstract class BaseRepository<T> {
	
	private Class<T> clazz;
	protected final EntityManager em;
	
    protected BaseRepository(Class<T> clazz, JpaContext context) {
        this.clazz = clazz;
	    this.em = context.getEntityManagerByManagedType(User.class);
    }
    
    /**
     * Generic method that searches for entities taking into account the filter parameters.
     * @param session {@link Session} Hibernate session.
     * @param filter {@link BaseFilter} Filter object to be used for setting the search conditions.
     * @param page {@link Pageable} Object for pagination information.
     * @return {@link Page} Paginated search result.
     */
	public Page<T> find(Session session, BaseFilter filter, Pageable page) {
		CriteriaQuery<T> criteria = createBaseCriteria(session, filter);
	    TypedQuery<T> query = createTypedQuery(session, criteria, page);
	    
	    return PageableExecutionUtils.getPage(query.getResultList(), page, () -> count(session, criteria));
	}
    
	/**
	 * Generic method that creates the search criteria query.
	 * @param session {@link Session} Hibernate session.
	 * @param filter {@link BaseFilter} Filter object to be used for setting the search conditions.
	 * @return {@link CriteriaQuery}
	 */
	private CriteriaQuery<T> createBaseCriteria(Session session, BaseFilter filter) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
	    CriteriaQuery<T> criteria = builder.createQuery(clazz);
	    Root<T> root = criteria.from(clazz);

	    List<Predicate> predicates = getFilterConditions(builder, criteria, root, filter);
	    if (!predicates.isEmpty()) {
		    criteria.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
	    }
	    
	    criteria.orderBy(QueryUtils.toOrders(new Sort(filter.getOrderDirection(), filter.getOrderField()), root, builder));
	    return criteria;
	}
	
	/**
	 * Generic method that creates the typed query.
	 * @param session {@link Session} Hibernate session.
	 * @param filter {@link BaseFilter} Filter object to be used for setting the search conditions.
	 * @param page {@link Pageable} Object for pagination information.
	 * @return {@link TypedQuery}
	 */
	private TypedQuery<T> createTypedQuery(Session session, CriteriaQuery<T> criteria, Pageable page)  {
		TypedQuery<T> query = session.createQuery(criteria);
	    query.setFirstResult(page.getPageNumber() * page.getPageSize());
	    query.setMaxResults(page.getPageSize());
	    return query;
	}
	
	/**
	 * Gets the total number of results for the given search criteria.
	 * @param session {@link Session} Hibernate session.
	 * @param query {@link CriteriaQuery} Original criteria query.
	 * @return {@link Long}
	 */
	private Long count(Session session, CriteriaQuery<T> query) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
	    CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

	    Root<T> root = criteria.from(query.getResultType());
		setRootJoins(root);
	    Predicate restriction = query.getRestriction();
	    if (restriction != null) {
	    	criteria.where(restriction);
	    }

	    criteria.select(builder.count(root));
	    return session.createQuery(criteria).getSingleResult();
	}

	/**
	 * Establishes root path join relationships.
	 * @param root {@link Root} Root type entity in the from clause.
	 */
	protected abstract void setRootJoins(Root<T> root);
	
	/**
	 * Creates the list of predicates for filtering by the given criteria.
	 * @param builder {@link CriteriaBuilder} Used to construct criteria queries.
	 * @param criteria {@link CriteriaQuery} Original criteria query.
	 * @param root {@link Root} Root type entity in the from clause.
	 * @param filter {@link BaseFilter} Filter object to be used for setting the search conditions.
	 */
	protected abstract List<Predicate> getFilterConditions(CriteriaBuilder builder, CriteriaQuery<T> criteria, Root<T> root, BaseFilter filter);
	
}
