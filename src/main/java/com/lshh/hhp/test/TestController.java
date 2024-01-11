package com.lshh.hhp.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping("/error")
    public Object invokeError(){
        throw new IllegalArgumentException("test invokeError");
    }

    @GetMapping("/error/{msg}")
    public Object invokeError2(String msg){
        throw new IllegalArgumentException("test invokeErro2");
    }
}
