package com.deveuge.quizial.util.aspect;

import javax.persistence.EntityManager;
import javax.persistence.PostPersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import org.springframework.transaction.annotation.Transactional;

import com.deveuge.quizial.enums.ActivityAction;
import com.deveuge.quizial.model.entity.Activity;
import com.deveuge.quizial.util.BeanUtil;

public class AuditableEntityListener {
	
	@PostPersist
    public void prePersist(AuditableEntity target) { 
		perform(target, ActivityAction.CREATE);
	}

    @PreUpdate
    public void preUpdate(AuditableEntity target) {
		perform(target, ActivityAction.UPDATE);
	}

    @PreRemove
    public void preRemove(AuditableEntity target) {
		perform(target, ActivityAction.DELETE);
	}
    
    @Transactional
    private void perform(AuditableEntity target, ActivityAction action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        Activity activity = Activity.builder()
        	.action(action)
        	.object(target.getClass().getCanonicalName())
        	.objectId(target.getAuditableId())
        	.build();
        entityManager.persist(activity);
    }
}
