package com.example.test.demo.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class TestInterceptor implements HandlerInterceptor {

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     *  컨트롤로 실행 직전에 동작
     *  반환값이 true일 경우 정상적으로 진행되고,false일 경우 실행이 멈춘다. ( 컨트롤러에 진입하지 않음 )
     *  Object handler는 핸들러 매핑이 찾은 컨트롤러 클래스 객체
     *  */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Interceptor > preHandle");
        return true;
    }

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     * 컨트롤러 진입 후 view가 렌더링 되기전 수행된다.
     * 전달인자의 modelAndView를 통해 화면 단에 들어가는 데이터 등의 조작이 가능하다.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
        log.info("Interceptor > postHandle");
    }

    /**
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     * 컨트롤러 진입 후 view가 정상적으로 랜더링 된 후 제일 마지막에 실행된다.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,  Object handler, Exception ex) throws Exception {
        log.info("Interceptor > afterCompletion");
    }
}
