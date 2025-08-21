package com.example.application_project.controller.application;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    @GetMapping("/test")
    public String test() {
        return "ApplicationController 동작 확인";
    }
}
