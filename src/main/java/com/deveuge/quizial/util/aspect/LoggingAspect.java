package com.deveuge.quizial.util.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

	@Around("@annotation(com.deveuge.quizial.util.aspect.MethodLogging)")
	public Object entryExitLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Signature signature = proceedingJoinPoint.getSignature();
	    log.info(">>> IN: {} {} ", signature.getDeclaringTypeName(), signature.getName());
	    Object proceed = proceedingJoinPoint.proceed();
	    log.info("<<< OUT: {} {} ", signature.getDeclaringTypeName(), signature.getName());
	    return proceed;
	}

}