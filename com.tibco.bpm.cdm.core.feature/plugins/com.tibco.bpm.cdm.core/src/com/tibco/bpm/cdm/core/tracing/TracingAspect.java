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

/**
 * Aspect for tracing method executions across different layers of the application.
 * Created: 2025-03-06
 */
@Aspect
public class TracingAspect implements Advice{
    // Replace Logger with CLFClassContext
    private static final CLFClassContext logCtx = CloudLoggingFramework.init(TracingAspect.class, CDMLoggingInfo.instance);

    private InstrumentationConfig instrumentationConfig;

    // Thread-safe map for storing execution times
    private final ConcurrentHashMap<String, ThreadLocal<Long>> executionTimes = new ConcurrentHashMap<>();

    // Initialize ThreadLocals for different layers
    {
        executionTimes.put("controller", ThreadLocal.withInitial(() -> 0L));
        executionTimes.put("service", ThreadLocal.withInitial(() -> 0L));
        executionTimes.put("repository", ThreadLocal.withInitial(() -> 0L));
    }

    public void setInstrumentationConfig(InstrumentationConfig instrumentationConfig) {
    	System.out.println("SetInst Method");
        this.instrumentationConfig = instrumentationConfig;
        System.out.println("TracingAspect setInstrumentationConfig()");
        System.out.println(instrumentationConfig); 
    }

    @Around("execution(* com.tibco.bpm.cdm.core.rest.v1.*.*(..))")
    public Object measureControllerTime(ProceedingJoinPoint joinPoint) throws Throwable {
    	System.out.println("com.tibco.bpm.cdm.core.rest.v1.");
        return instrumentMethod(joinPoint, "controller");
    }

    @Around("execution(* com.tibco.bpm.cdm.core.*.*(..))")
    public Object measureServiceTime(ProceedingJoinPoint joinPoint) throws Throwable {
    	System.out.println("com.tibco.bpm.cdm.core.");
        return instrumentMethod(joinPoint, "service");
    }

    @Around("execution(* com.tibco.bpm.cdm.core.dao.*.*(..))")
    public Object measureRepositoryTime(ProceedingJoinPoint joinPoint) throws Throwable {
    	System.out.println("com.tibco.bpm.cdm.core.dao.");
        return instrumentMethod(joinPoint, "repository");
    }

    private Object instrumentMethod(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        // Create a method-specific logging context
        CLFMethodContext clf = logCtx.getMethodContext("instrumentMethod");

        if (!instrumentationConfig.isEnabled()) {
        	clf.local.debug("Instrumentation disabled. Skipping tracing for method: "+ layer);
        	System.out.println("Instrumentation disabled. Skipping tracing for method: "+ layer);
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
            handleException(clf, e);
            throw e;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            updateExecutionTimes(layer, executionTime);
            logExecutionDetails(clf, layer, spanName, executionTime);

            // Clean up if this is the controller layer
            if ("controller".equals(layer)) {
                cleanupThreadLocals();
            }
        }
    }

    private void handleException(CLFMethodContext clf, Throwable e) {
        clf.local.error("Exception in traced method: " + e.getMessage(), e);
        System.out.println("Exception in traced method: " + e.getMessage());
    }

    private void updateExecutionTimes(String layer, long executionTime) {
        ThreadLocal<Long> timeHolder = executionTimes.get(layer);
        if (timeHolder != null) {
            timeHolder.set(executionTime);
        }
    }

    private void logExecutionDetails(CLFMethodContext clf, String layer, String methodName, long executionTime) {
        clf.local.debug("{" + layer + "} Layer - {" + methodName + "} executed in ms: "+ executionTime);
        System.out.println("{" + layer + "} Layer - {" + methodName + "} executed in ms: "+ executionTime);

        if ("controller".equals(layer)) {
            long totalTime = executionTimes.values().stream()
                    .mapToLong(threadLocal -> threadLocal.get())
                    .sum();
            clf.local.debug("Total execution time (Controller + Service + Repository) ms: ",totalTime);
            System.out.println("Total execution time (Controller + Service + Repository) ms: "+totalTime);
        }
    }

    private void cleanupThreadLocals() {
        executionTimes.values().forEach(ThreadLocal::remove);
    }
}