package com.lshh.hhp.biz;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface Biz {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";

//    Class<?> master();
    int level() default 0;
}