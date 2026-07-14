package com.example.supportticket.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class ExecutionTimeAspect {

    private static final Logger log = LoggerFactory.getLogger(ExecutionTimeAspect.class);

    @Around("@annotation(com.example.supportticket.aop.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startNanos = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
            log.info("{} completed in {} ms", joinPoint.getSignature().toShortString(), elapsedMillis);
        }
    }
}
