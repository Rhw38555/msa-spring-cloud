package com.example.zuulservice.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class ZuulLoggingFilter extends ZuulFilter {


    @Override
    public Object run() throws ZuulException {
        log.info("********************* printing logs: ");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("********************* " + request.getRequestURI());
        return null;
    }

    @Override
    public String filterType() {
        return "pre"; // 사전 / 사후 필터 정의
    }

    @Override
    public int filterOrder() {
        return 1; // 필터 순서
    }

    @Override
    public boolean shouldFilter() {
        return true; // 필터로 사용하겠다
    }
}
