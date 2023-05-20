package com.deveuge.quizial.util.aspect;

import com.deveuge.quizial.model.entity.User;

public interface AuditableEntity {

	String getAuditableId();
	User getUser();
}
