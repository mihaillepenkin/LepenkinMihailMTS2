package org.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Before("execution(* org.example.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Перед вызовом метода: " +
                joinPoint.getSignature().getName());
    }

    @Around("execution(* org.example.controller.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint)
            throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - startTime;
        log.info("Метод " + joinPoint.getSignature().getName()
                + " выполнен за " + duration + " мс");
        return result;
    }
}