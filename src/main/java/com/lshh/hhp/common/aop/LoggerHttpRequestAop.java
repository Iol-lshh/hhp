package com.lshh.hhp.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggerHttpRequestAop {
    @Pointcut("within(@org.springframework.stereotype.Controller *) " +
            "|| within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {
    }

    @Before("controller()")
    public void logController(JoinPoint joinPoint) throws Throwable {
        log.info("[HttpRequest] Before " + joinPoint.toString());
    }
}
