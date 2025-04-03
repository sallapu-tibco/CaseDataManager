package com.tibco.bpm.cdm.core.tracing;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.aop.Advice;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.tibco.bpm.cdm.core.logging.CDMLoggingInfo;
import com.tibco.bpm.logging.cloud.api.CLFClassContext;
import com.tibco.bpm.logging.cloud.api.CloudLoggingFramework;
import com.tibco.bpm.logging.cloud.context.CLFMethodContext;

@Aspect
public class ExecutionTimeAspect implements Advice {
    private static final CLFClassContext logCtx = CloudLoggingFramework.init(ExecutionTimeAspect.class, CDMLoggingInfo.instance);

    private InstrumentationConfig instrumentationConfig;

    private final ConcurrentHashMap<String, ThreadLocal<Long>> executionTimes = new ConcurrentHashMap<>();

    {
        executionTimes.put("controller", ThreadLocal.withInitial(() -> 0L));
        executionTimes.put("service", ThreadLocal.withInitial(() -> 0L));
        executionTimes.put("repository", ThreadLocal.withInitial(() -> 0L));
    }

    public void setInstrumentationConfig(InstrumentationConfig instrumentationConfig) {
        this.instrumentationConfig = instrumentationConfig;
    }

    @Around("execution(* com.tibco.bpm.cdm.core.rest.v1..*(..))")
    public Object logControllerTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, "controller");
    }

    @Around("execution(* com.tibco.bpm.cdm.core.service..*(..))")
    public Object logServiceTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, "service");
    }

    @Around("execution(* com.tibco.bpm.cdm.core.repository..*(..))")
    public Object logRepositoryTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, "repository");
    }

    private Object logExecutionTime(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        CLFMethodContext clf = logCtx.getMethodContext("logExecutionTime");

        if (!instrumentationConfig.isEnabled()) {
            return joinPoint.proceed();
        }

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = method.getName();
        String className = method.getDeclaringClass().getSimpleName();
        String spanName = String.format("%s.%s", className, methodName);

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            System.out.println("Exception in traced method: " + e.getMessage());
            throw e;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            updateExecutionTimes(layer, executionTime);
            logExecutionDetails(clf, layer, spanName, executionTime);

            if ("controller".equals(layer)) {
                cleanupThreadLocals();
            }
        }
    }

    private void updateExecutionTimes(String layer, long executionTime) {
        ThreadLocal<Long> timeHolder = executionTimes.get(layer);
        if (timeHolder != null) {
            timeHolder.set(executionTime);
        }
    }

    private void logExecutionDetails(CLFMethodContext clf, String layer, String methodName, long executionTime) {
        System.out.println("{" + layer + "} Layer - {" + methodName + "} executed in ms: " + executionTime);

        if ("controller".equals(layer)) {
            long totalTime = executionTimes.values().stream()
                    .mapToLong(ThreadLocal::get)
                    .sum();
            System.out.println("Total execution time (Controller + Service + Repository) ms: " + totalTime);
        }
    }

    private void cleanupThreadLocals() {
        executionTimes.values().forEach(ThreadLocal::remove);
    }
}