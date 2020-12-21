### Filter와 Interceptor를 비교
 - Filter는 DispatcherServlet을 타기전에 먼저 거친다. 주로, 전역적인 필터링을 위해 사용된다. ( ex. 특정 ip 차단, 인코딩 변환처리.. 등등 )
 - 반면 Interceptor는 DispatcherServlet이 Controller를 호출하기 전, 후로 호출된다. 주로, 특정 컨트롤러의 공통모듈을 뺄때 사용한다. ( ex. 로깅, 유저의 세션체크..등등 )
 
![캡처](https://user-images.githubusercontent.com/21052356/102745880-65ff7180-43a0-11eb-8f79-8936d172caaa.png)


### Filter 적용 방법
 - @WebFilter 에너테이션 필터 등록

// TestFilter.java
- 아래 코드에서는 urlPatterns를 "*"로 했으므로 모든 요청 Filter를 거친다. 특정 urlPattern만 거치게 하고 싶으면 url 패턴을 추가한다. ( 만약, urlPattern="/aaa/*" 라고 설정했다면 해당 서버로 들어오는
url 패턴중 "/aaa"의 패턴만 Filter를 거치게 된다. 즉, "/aaa" 요청은 Filter를 타고 controller로 전달이 되고, 다른 요청들은 Filter를 안타고 Controller로 전달이 된다.
- 만약, 특정 ip를 차단하고 싶다면 @WebFilter에 모든 요청을 허용하고 (@WebFilter(urlPatterns="*")) doFilter에서 인자로 들어오는 servletRequest를 통해 ip를 알아내서 dofilter에서 ip를 걸러주면 될거 같다.

```java
@Slf4j
@WebFilter(urlPatterns = "*") // 모든 요청이 다 filter에 걸린다. 만약,특정 경로의 요청만 걸리게 하고 싶으면 해당 경로를 적어주면 된다. ex) "/test/*"
public class TestFilter implements Filter {

    @Override -> 서버가 떴을때  1번 실행
    public void init(FilterConfig filterConfig) throws ServletException { 
        log.info("init TestFilter");
    }

    @Override -> 매 요청마다 실행 ( dofilter로 dispatcherSevlet에게 요청값을 전달 )
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        log.info("##### filter - before ######");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        log.info("요청 URL : " + req.getRequestURI());

        filterChain.doFilter(servletRequest, servletResponse);

        log.info("##### filter - after #####");
    }

    @Override -> 서버가 내려갈때 1 번 실행
    public void destroy() {
        log.info("destroy TestFilter");
    }
}
```

// DemoApplication.java
```java
@ServletComponentScan 
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```


### Interceptor
  
  - HandlerInterceptor 구현한다. ( preHandle, postHandle, afterCompletion )
  - preHandle는 컨트롤러 진입전, view 랜더링 전, afterCompletion는 view 랜더링 후에 호출된다. ( 주로 세션체크를 위해 preHandle만 사용해봄 )
  ```java
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
  ```
  - config 설정 -> webMvcConfiger의 addInterceptor 함수를 구현해준다.  addPathPatterns에 url pattern을 등록하면 해당 url 패턴으로 요청할 경우 interceptor를 타게된다. 
excludePathPatterns에 등록하면 interceptor를 타지 않는다. addPathPatterns에 등록안하면 interceptor 안 타므로 굳이 excludePathPatterns에 등록해줄 필요는 없는거 같음.
    
  // WebMvcConfig.java
  ```java
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
}
  ```
  

### 추가적으로 WebMvcConfigurer 인터페이스의 메소드 설명
 - addResourceHandlers
   - 정적인 리소스를 호스팅할때 설정해준다.
   - 아래 코드는 "/test/*"로 요청이 들어오면 "/resource/views/"안에서 리소스를 찾는다.
   ```java
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/test/**")  // /test/** 로 요청을 받으면 /views/** 의 값으로 응답을 한다.
                .addResourceLocations("classpath:/views/");
    }
   ```
   
 - addCorsMappings
   - 서버에서 cors설정을 전역으로 할때 사용한다.
   ```java
    // global cors
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 요청에 대해서
                .allowedOrigins("*") // 허용할 origin 리스트 ( 모두 허용 )
                .allowedMethods("GET", "POST")
    }
    ```
    - 특정 컨트롤러에만 cors를 처리하려면 @@crossorigin를 붙여준다.
    ```java
     @CrossOrigin
     @RestController
     public class TestFilterController {

     @GetMapping("/filter")
     public void filter_test( ) {
         log.info("filter_test");
     }
   }
```
    
   
   
