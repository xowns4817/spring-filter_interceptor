package com.example.test.demo.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestFilterController {

    @GetMapping("/filter")
    public void filter_test( ) {
        log.info("filter_test");
    }
}
