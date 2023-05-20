package com.deveuge.quizial.util.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ExceptionHandlingAspect {

	@Pointcut("within(com.deveuge.quizial..*) && execution(* *(..))")
	public void matchAllMyMethods() {}

	@AfterThrowing(value = "matchAllMyMethods()", throwing = "exception")
	public void logException(JoinPoint joinPoint, Throwable exception) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();
        
        log.error("--- ERROR: {} {} {}", className, methodName, methodArgs);
        if(exception instanceof ResponseStatusException) {
            log.error("--- ERR. DETAILS: {} {}", exception.getClass(), exception.getMessage());
        } else {
            log.error("--- ERR. DETAILS: ", exception);
        }
	}
}