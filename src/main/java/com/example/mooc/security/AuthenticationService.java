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
        String principal = loginRequest.principal();
        String credentials = loginRequest.credentials();
        UserModel user = userService.loadUserByPrincipal(principal);

        validateCredentials(credentials, user.getPassword());
        user.setPassword(null);

        return JweService.generateToken(user);
    }

    public void validateCredentials(String credentials, String encryptedCredentials) {
         if (!passwordEncoder.matches(credentials, encryptedCredentials)) {
             throw new AuthInvalidException();
         }
    }

}
