package com.example.mooc.controller;


import com.example.mooc.dto.request.LoginRequest;
import com.example.mooc.dto.response.LoginResponse;
import com.example.mooc.security.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationService authenticationService;

    @PostMapping(value = "/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return new LoginResponse(authenticationService.authenticate(loginRequest));
    }

}