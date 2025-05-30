package com.protim.course.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
@Component
public class ServiceLayerAspect {

    @Before("execution(* com.protim.course.service.*.*(..))")
    public void before(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        List<Object> argsList = Arrays.asList(joinPoint.getArgs());

        log.info("Entering {}.{}() | arguments: {}", className, methodName, argsList);
    }

    @AfterReturning(pointcut = "execution(* com.protim.course.service.*.*(..))", returning = "result")
    public void after(JoinPoint joinPoint, Object result){
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("Exiting {}.{}() | Response: {}", className, methodName, result);
    }
}
