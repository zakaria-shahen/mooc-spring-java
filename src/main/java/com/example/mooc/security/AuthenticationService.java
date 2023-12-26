package com.example.mooc.security;

import com.example.mooc.dto.request.LoginRequest;
import com.example.mooc.exception.AuthInvalidException;
import com.example.mooc.model.UserModel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationService {
    private UserService userService;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public String authenticate(LoginRequest loginRequest) throws AuthenticationException {
        UserModel user = userService.loadUserByPrincipalAndApplyLoginAttempts(loginRequest.principal());

        validateCredentials(loginRequest.credentials(), user.getPassword());
        user.setPassword(null);
        userService.resetLoginAttempts(user.getEmail());

        return JweService.generateToken(user);
    }

    public void validateCredentials(String credentials, String encryptedCredentials) {
         if (!passwordEncoder.matches(credentials, encryptedCredentials)) {
             throw new AuthInvalidException();
         }
    }

}
