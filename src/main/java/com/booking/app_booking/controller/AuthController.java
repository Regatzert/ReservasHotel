package com.booking.app_booking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.app_booking.dto.LoginRequest;
import com.booking.app_booking.dto.Response;
import com.booking.app_booking.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> loginUser(@RequestBody @Valid LoginRequest request) {
        
        return ResponseEntity.ok(userService.loginUser(request));
    }
    
}
