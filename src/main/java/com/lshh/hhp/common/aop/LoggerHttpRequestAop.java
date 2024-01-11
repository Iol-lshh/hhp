package com.lshh.hhp.common.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lshh.hhp.common.ThreadTraceHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;

@Slf4j
@Aspect
@Component    // 필터로 처리하자
public class LoggerHttpRequestAop {

    // @Aspect vs MethodInterceptor(Advice)
    // 1. @Aspect
    // @Aspect 어노테이션은 Spring 2.0부터 도입된 어노테이션 기반의 AOP를 지원합니다.
    // 이 방식은 가장 일반적으로 사용되며,
    // 클래스를 Aspect로 표시하고 @Before, @After, @Around 등의 어노테이션을 메서드에 적용하여
    // 타겟 메서드 전후에 수행할 부가적인 작업을 정의합니다.
    // 이방식은 클래스와 메서드의 코드에 직접 어노테이션을 사용하므로 알아보기도 쉽고 관리하기도 좋습니다.
    // 2. MethodInterceptor
    // MethodInterceptor는 Spring의 초기 AOP 지원 방식으로, 메서드 호출을 가로채는 인터페이터입니다.
    // 'Around Advice'라는 것을 구현하게 되는데 이것은 타겟 메서드 호출 전후로 코드를 실행할 수 있게 해줍니다.
    // MethodInterceptor를 구현한 Advice 클래스는 ProxyFactoryBean에 등록되거나
    // ProxyFactory를 사용해서 프록시 생성 시 적용될 수 있습니다.
    // 하지만 이 방식은 메서드가 호출되는 모든 상황을 다루는 'around' 유형의 advice만을 지원하게 됩니다.

    // execution: 메서드 실행 조인 포인트 매칭
    // within: 특정 타입 내의 조인 포인트 매칭
    // args: 인자가 주어진 타입의 인스턴스인 조인 포인트
    // this: 스프링 빈 개체(AOP 프록시)를 대상으로 하는 조인 포인트
    // target: Target 객체(프록시가 가르키는 실제 대상)을 대상으로 하는 조인 포인트
    // @Target: 실행 객체의 클래스에 주어진 타입의 어노테이션이 있는 조인 포인트
    // @within : 주어진 어노테이션이 있는 타입 내 조인 포인트
    // @annotation : 메서드가 주어진 어노테이션을 가지고 있는 조인 포인트를 매칭
    // @args : 전달된 실제 인수의 런타임 타입이 주어진 타입의 어노테이션을 갖는 조인 포인트
    // bean : 스프링 전용 포인트컷 지시자, 빈의 이름으로 포인트컷을 지정
    @Pointcut("within(@org.springframework.stereotype.Controller *) " +
            "|| within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {
    }

//    @Before("controller()")
//    public void logController(JoinPoint joinPoint) throws Throwable {
//        log.info("[HttpRequest] Before-Controller " + joinPoint.toString());
//    }

    // @Around ProceedingJoinPoint -> proceed() 있다.
    // @Before, @After -> JoinPoint -> proceed() 없다.
    @Around("controller()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        log.info(String.format("""
        { "traceId": "%s", "payload": %s }"""
                , ThreadTraceHelper.getTraceId()
                , String.join(",", Arrays.stream(joinPoint.getArgs()).map(o-> {
                    try {
                        return new ObjectMapper().writeValueAsString(o);
                    } catch (JsonProcessingException e) {
                        return "";
                    }
                }).toList())));
        result = joinPoint.proceed();
        return result;
    }
}