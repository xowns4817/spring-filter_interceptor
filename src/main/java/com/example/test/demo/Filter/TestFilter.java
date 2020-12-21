package com.example.test.demo.Filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "*") // 모든 요청이 다 filter에 걸린다. 만약,특정 경로의 요청만 걸리게 하고 싶으면 해당 경로를 적어주면 된다. ex) "/test/*"
public class TestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init TestFilter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        log.info("##### filter - before ######");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        log.info("요청 URL : " + req.getRequestURI());

        filterChain.doFilter(servletRequest, servletResponse);

        log.info("##### filter - after #####");
    }

    @Override
    public void destroy() {
        log.info("destroy TestFilter");
    }
}
