package com.project.library.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.project.library.services.impl.*.*(..))")
    private void publicMethodsForLoggingInServicesImpl(){}

    @Pointcut("execution(public * com.project.library.controllers.*.*(..))")
    private void publicMethodsForLoggingInControllers(){}

    @Before(value = "publicMethodsForLoggingInControllers()")
    public void logBeforeControllers(JoinPoint joinPoint) {
        logger.info(">> Controller method is being called: {}() - {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @Around(value = "publicMethodsForLoggingInServicesImpl()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        logger.info(">> {} {}() - {}", joinPoint.getSignature().getDeclaringType() , methodName, Arrays.toString(args));
        Object result = joinPoint.proceed();
        logger.info("<< {}() - {}", methodName, result);
        return result;
    }
}
