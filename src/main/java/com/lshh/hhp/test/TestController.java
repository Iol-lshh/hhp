package com.lshh.hhp.test;

import com.lshh.hhp.common.exception.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping("/business")
    public Object invokeBusinessException(){
        throw new BusinessException("test invokeBusinessException");
    }

    @GetMapping("/business/{msg}")
    public Object invokeBusinessException2(@PathVariable String msg){
        throw new BusinessException(msg);
    }

    @GetMapping("/error")
    public Object invokeError() throws Exception {
        throw new Exception("test invokeError");
    }

    @GetMapping("/error/{msg}")
    public Object invokeError2(@PathVariable String msg) throws Exception {
        throw new Exception(msg);
    }
}
