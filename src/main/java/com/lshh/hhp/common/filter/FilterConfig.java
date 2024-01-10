package com.lshh.hhp.common.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<LoggerHttpRequestFilter> loggingFilter() {
        FilterRegistrationBean<LoggerHttpRequestFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new LoggerHttpRequestFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}