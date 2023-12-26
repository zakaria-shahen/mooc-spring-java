package com.example.mooc.controller;


import com.example.mooc.dto.request.LoginRequest;
import com.example.mooc.dto.request.RegistrationRequest;
import com.example.mooc.dto.response.LoginResponse;
import com.example.mooc.model.UserModel;
import com.example.mooc.security.AuthenticationService;
import com.example.mooc.security.JweService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return new LoginResponse(authenticationService.authenticate(loginRequest));
    }

    @PostMapping("/user/registration")
    public Map<String, String> userRegistration(@RequestBody RegistrationRequest registrationRequest) {
        authenticationService.userRegistration(registrationRequest, UserModel.ROLE.USER);
        return Map.of("status", "created successfully");
    }

    @PostMapping("/publisher/registration")
    public Map<String, String> publisherRegistration(@RequestBody RegistrationRequest registrationRequest) {
        authenticationService.userRegistration(registrationRequest, UserModel.ROLE.PUBLISHER);
        return Map.of("status", "created successfully");
    }

    @PostMapping("/token")
    public LoginResponse newToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        var BEARER = "bearer";
        token = token.substring(token.indexOf(BEARER) + 1);
        return new LoginResponse(JweService.generateToken(token));
    }

}