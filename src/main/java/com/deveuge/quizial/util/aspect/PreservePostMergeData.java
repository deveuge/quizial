package com.deveuge.quizial.util.aspect;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class PreservePostMergeData {

    @Around("execution(public !void javax.persistence.EntityManager.merge(..))")
    private Object preserveTransientDataPostMerge(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        Object afterMerge = joinPoint.proceed();
        if (args.length > 0) {
            Object beforeMerge = args[0];

            Field[] annotatedFieldsToPreserve = FieldUtils.getFieldsWithAnnotation(beforeMerge.getClass(), PreservePostMerge.class);
            Arrays.stream(annotatedFieldsToPreserve).forEach(field -> {
                try {
                    FieldUtils.writeField(field, afterMerge, FieldUtils.readField(field, beforeMerge, true), true);
                } catch (IllegalAccessException exception) {
                    log.warn("Illegal accesss to field: {}, of entity: {}. Data was not preserved.", field.getName(), beforeMerge.getClass());
                }
            });
        }

        return afterMerge;
    }
}
