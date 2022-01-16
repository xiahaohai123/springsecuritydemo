package com.example.springsecuritydemo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/pojo")
    public ResponseEntity<?> testPOJO(@RequestBody POJO pojo) {
        System.out.println(pojo);
        return ResponseEntity.ok(pojo);
    }
}
