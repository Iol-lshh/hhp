package com.lshh.hhp.common.aop;

import com.lshh.hhp.common.ThreadTraceHelper;
import com.lshh.hhp.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Aspect
@Component
public class EventListenerAop {

    @Around("@annotation(org.springframework.context.event.EventListener)")
    public Object onEvent(final ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = Instant.now().toEpochMilli();
        String traceId = ThreadTraceHelper.getTraceId();
//        String eventName = joinPoint.getArgs()[0].getClass().getName();
        String eventName = joinPoint.getSignature().getName();

        log.info(String.format("""
            { "traceId": "%s", "event":"%s"}"""
                , traceId
                , eventName));

        try{
            joinPoint.proceed();

        } catch (BusinessException exception){
            log.info(String.format("""
                { "traceid": "%s", "event":"%s", "exception": "%s", "msg": "[비즈니스 예외] - %s" }""", traceId, eventName, "BusinessException", exception.getMessage()));
            log.debug("", exception);

        } catch (ObjectOptimisticLockingFailureException exception){
            log.warn(String.format("""
                { "traceid": "%s", "event":"%s", "exception": "%s", "msg": "[낙관적락 예외] - %s" }""", traceId, eventName, "ObjectOptimisticLockingFailureException", exception.getMessage()));
            log.debug("", exception);

        } catch (Exception exception){
            log.error(String.format("""
                { "traceid": "%s", "event":"%s", "msg": "[error] - %s" }""", traceId, eventName, exception.getMessage()));
            log.debug("", exception);

        } finally {
            long endTime = Instant.now().toEpochMilli();
            log.info(String.format("""
                            { "traceId": "%s", "event":"%s", "executionTime": %d }"""
                    , traceId
                    , eventName
                    , endTime - startTime));

        }
        return joinPoint;
    }
}
