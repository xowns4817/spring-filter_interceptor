package com.example.test.demo.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestInterceptorController {

    @GetMapping("/aaa")
    public void aaa( ) {
        log.info("aaa");
    };

    @GetMapping("/bbb")
    public void bbb( ) {
        log.info("bbb");
    }
}
