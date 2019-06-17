package com.chen.concurrency.threadlocal;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * HttpFilter
 * <p>
 * @Author LeifChen
 * @Date 2019-06-17
 */
@Slf4j
public class HttpFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        log.info("do filter, {}, {}", Thread.currentThread().getId(), httpServletRequest.getServletPath());
        RequestHolder.add(Thread.currentThread().getId());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
