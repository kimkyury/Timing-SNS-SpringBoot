package com.kkukku.timing.apis.test.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Test", description = "Test API")
public class TestController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
