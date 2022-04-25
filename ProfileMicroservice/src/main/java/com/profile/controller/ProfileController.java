package com.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/profile")
public class ProfileController {

    @GetMapping(value = "/hello")
    public ResponseEntity<String> login() {
        System.out.println("Hello");
        return ResponseEntity.ok("Hello");
    }
}
