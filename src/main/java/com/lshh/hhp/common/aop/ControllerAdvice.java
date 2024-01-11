package com.lshh.hhp.common.aop;

import com.lshh.hhp.common.ThreadTraceHelper;
import com.lshh.hhp.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public <T> ResponseEntity<T> onException(BusinessException exception){
        String traceId = ThreadTraceHelper.getTraceId();
        log.error(String.format("""
                { "traceid": "%s", "msg": "%s" }""", traceId, exception.getMessage()));
        log.debug("", exception);
        return ResponseEntity.internalServerError().header("msg", exception.getMessage()).build();
    }
}
