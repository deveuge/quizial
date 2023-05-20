package com.deveuge.quizial.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandlerController {

	@ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex) {
		return "error/403";
    }
	
	@ExceptionHandler(NoHandlerFoundException.class)
    public String handleNoHandlerFoundException(NoHandlerFoundException ex) {
		return "error/404";
    }
	
	@ExceptionHandler(Exception.class)
    public String handleGenericExceptionException(Exception ex) {
		return "error/500";
    }
	
	@ExceptionHandler(ResponseStatusException.class)
	public String handleResponseStatusException(ResponseStatusException ex) {
		if(HttpStatus.NOT_FOUND.equals(ex.getStatus())) {
			return "error/404";
		}
		return null;
	}
}