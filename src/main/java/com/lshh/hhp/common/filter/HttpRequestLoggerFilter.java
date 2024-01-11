package com.lshh.hhp.common.filter;

import com.lshh.hhp.common.ThreadTraceHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Instant;

@Slf4j
public class HttpRequestLoggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long startTime = Instant.now().toEpochMilli();

        HttpServletRequest hsr = (HttpServletRequest) servletRequest;
        String traceId = hsr.getRequestId() + "_" + startTime;

        log.info(String.format("""
                        { "traceId": "%s", "requestUrl": "%s", "method": "%s"}"""
                , traceId, hsr.getRequestURI(), hsr.getMethod()));

        try {
            // 추적 아이디 전달
            servletRequest.setAttribute("traceId", traceId);
            ThreadTraceHelper.setTraceId(traceId);
            // 동작
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            // finally는 어떤 경우(break, return, throw)에도, "무조건" 실행된다.
            ThreadTraceHelper.removeTraceId();
        }

        long endTime = Instant.now().toEpochMilli();
        long resultTime = endTime - startTime;
        log.info(String.format("""
            { "traceId": "%s", "resultTime": "%d ms" }"""
                , traceId, resultTime));
    }
}
