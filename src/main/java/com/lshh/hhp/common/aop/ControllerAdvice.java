package com.lshh.hhp.common.aop;

import com.lshh.hhp.common.ThreadTraceHelper;
import com.lshh.hhp.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public <T> ResponseEntity<T> onBusinessException(BusinessException exception){
        String traceId = ThreadTraceHelper.getTraceId();
        log.info(String.format("""
                { "traceid": "%s", "exception": "%s", "msg": "[비즈니스 예외] - %s" }""", traceId, "BusinessException", exception.getMessage()));
        log.debug("", exception);
//        return ResponseEntity.ok().header("msg", exception.getMessage()).build();
        return ResponseEntity.noContent().header("msg", exception.getMessage()).build();
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public <T> ResponseEntity<T> onObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException exception){
        String traceId = ThreadTraceHelper.getTraceId();
        log.warn(String.format("""
                { "traceid": "%s", "exception": "%s", "msg": "[낙관적락 예외] - %s" }""", traceId, "ObjectOptimisticLockingFailureException", exception.getMessage()));
        log.debug("", exception);
//        return ResponseEntity.ok().header("msg", exception.getMessage()).build();

        return ResponseEntity.unprocessableEntity().header("msg", exception.getMessage()).build();
    }
    
    @ExceptionHandler(Exception.class)
    public <T> ResponseEntity<T> onException(Exception exception){
        String traceId = ThreadTraceHelper.getTraceId();
        log.error(String.format("""
                { "traceid": "%s", "msg": "%s" }""", traceId, exception.getMessage()));
        log.debug("", exception);
        return ResponseEntity.internalServerError().header("msg", exception.getMessage()).build();
    }
}
