package com.lshh.hhp.common.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<HttpRequestLoggerFilter> loggingFilter() {
        FilterRegistrationBean<HttpRequestLoggerFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new HttpRequestLoggerFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}