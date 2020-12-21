package com.example.test.demo.Interceptor.config;

import com.example.test.demo.Interceptor.TestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private TestInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/aaa")
                .excludePathPatterns("/bbb");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/test/**")  // /test/** 로 요청을 받으면 /views/** 의 값으로 응답을 한다.
                .addResourceLocations("classpath:/views/")
                .setCachePeriod(20);
    }

    // global cors
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 요청에 대해서
                .allowedOrigins("*") // 허용할 origin 리스트 ( 모두 허용 )
                .allowedMethods("GET", "POST")
                .maxAge(3000);
    }
}
