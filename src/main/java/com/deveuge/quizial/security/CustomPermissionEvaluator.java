package com.deveuge.quizial.security;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.deveuge.quizial.enums.PermissionEntity;
import com.deveuge.quizial.enums.Role;
import com.deveuge.quizial.util.SecurityUtils;
import com.deveuge.quizial.util.aspect.AuditableEntity;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
		if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
			return false;
		}
		if(hasAdminRole(auth)) {
			return true;
		}
		boolean hasOwnerPermissions = true;
		if(targetDomainObject instanceof AuditableEntity) {
			hasOwnerPermissions = isOwner(((AuditableEntity) targetDomainObject).getAuditableId(), targetDomainObject.getClass().getSimpleName());
		}
		return hasPrivilege(auth, permission.toString().toUpperCase()) && hasOwnerPermissions;
	}

	@Override
	public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
		if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
			return false;
		}
		if(hasAdminRole(auth)) {
			return true;
		}
		return hasPrivilege(auth, permission.toString().toUpperCase()) && isOwner(targetId, targetType);
	}
	
	/**
	 * Verifies that the user has the ADMIN role.
	 * @param auth {@link Authentication} Token for an authenticated principal.
	 * @return true if user is an administrator, false otherwise.
	 */
	private boolean hasAdminRole(Authentication auth) {
		return auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(Role.ADMIN.toString()));
	}
	
	/**
	 * Verifies that the user has the specified permission.
	 * @param auth {@link Authentication} Token for an authenticated principal.
	 * @param permission {@link String} A representation of the permission object as supplied by the expression system.
	 * @return true if the permission is granted, false otherwise.
	 */
	private boolean hasPrivilege(Authentication auth, String permission) {
	    for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
	        if (grantedAuth.getAuthority().equals(permission)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	/**
	 * Verifies that the user is the owner of the object.
	 * @param targetId {@link Serializable} The identifier for the object instance (usually a Long).
	 * @param targetType {@link String} The value of a {@link PermissionEntity} representing the object instance.
	 * @return true if the user is the owner, false otherwise.
	 */
	private boolean isOwner(Serializable targetId, String targetType) {
		PermissionEntity permission = PermissionEntity.valueOf(targetType.toUpperCase());
		AuditableEntity entity = (AuditableEntity) em.find(permission.getClazz(), targetId);
		return entity == null || entity.getUser().getUsername().equals(SecurityUtils.getUsername());
	}
}