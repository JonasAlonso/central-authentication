package com.baerchen.central.authentication.config.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ControllerLoggingAspect {

    private static final Logger requestLogger = LoggerFactory.getLogger("RequestLogger");

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {}

    @Around("restControllerMethods()")
    public Object logRestCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        requestLogger.info("Request: {} with args {}", method, Arrays.toString(args));
        try {
            Object result = joinPoint.proceed();
            requestLogger.info("Response from {}: {}", method, result);
            return result;
        } catch (Throwable ex) {
            requestLogger.error("Exception in {}: {}", method, ex.getMessage());
            throw ex;
        }
    }
}
