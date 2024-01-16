package com.lshh.hhp.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@org.springframework.stereotype.Service
public @interface Service {
    @AliasFor(annotation = Component.class)
    String value() default "";

//    Class<?> master();
    int level() default 0;
}