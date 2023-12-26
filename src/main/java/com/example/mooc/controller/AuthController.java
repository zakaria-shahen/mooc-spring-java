package com.example.mooc.controller;


import com.example.mooc.dto.request.LoginRequest;
import com.example.mooc.dto.request.RegistrationRequest;
import com.example.mooc.dto.request.RestCredentialsRequest;
import com.example.mooc.dto.request.SetCredentialsRequest;
import com.example.mooc.dto.response.LoginResponse;
import com.example.mooc.exception.AuthInvalidException;
import com.example.mooc.model.UserModel;
import com.example.mooc.security.AuthenticationService;
import com.example.mooc.security.JweService;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

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
        if (!Objects.equals(grantType, "refresh_token") || !Objects.equals(clientId, "web") || !Objects.equals(clientSecret, "secret")) {
            throw new AuthInvalidException();
        }

        return authenticationService.refreshTokenGrantType(refreshToken);
    }


    @PostMapping("/logout")
    public Map<String, String> logout(
            @RequestParam("refresh_token")
            String refreshToken,
            JwtAuthenticationToken principal
    ) {
        authenticationService.logout(
                principal.getToken().getTokenValue(),
                (long) principal.getTokenAttributes().get(JweService.ClaimNames.USER_ID),
                refreshToken
        );

        return Map.of("status", "Successfully logout");
    }

    @PostMapping("/credentials/reset")
    public Map<String, String> resetPassword(@RequestBody RestCredentialsRequest restCredentialsRequest) {
        if (restCredentialsRequest.receiverType() != null) {
            // support email only.
            throw new UnsupportedOperationException();
        }

        authenticationService.restCredentials(restCredentialsRequest);
        return Map.of("status", "Successfully sent reset credentials request");

    }

    @PostMapping("/credentials/set")
    public Map<String, String> resetPassword(@RequestBody SetCredentialsRequest setCredentialsRequest){

        authenticationService.setCredentials(setCredentialsRequest);
        return Map.of("status", "Successfully set credentials");

    }

}