package com.example.mooc.controller;


import com.example.mooc.dto.request.LoginRequest;
import com.example.mooc.dto.request.RegistrationRequest;
import com.example.mooc.dto.response.LoginResponse;
import com.example.mooc.exception.AuthInvalidException;
import com.example.mooc.model.UserModel;
import com.example.mooc.security.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
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
    public LoginResponse newToken(
            @RequestParam(value = "grant_type", defaultValue = "refresh_token")
            String grantType,
            @RequestParam("refresh_token")
            String refreshToken,
            @RequestParam(value = "client_id", defaultValue = "web")
            String clientId,
            @RequestParam(value = "client_secret", defaultValue = "secret")
            String clientSecret
    ) {
        if (grantType != "refresh_token" || clientId != "web" || clientSecret != "secret") {
            throw new AuthInvalidException();
        }

        return authenticationService.refreshTokenGrantType(refreshToken);
    }

}