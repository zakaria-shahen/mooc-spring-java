package com.example.mooc.security;

import com.example.mooc.exception.AuthExceedLoginAttemptsException;
import com.example.mooc.model.UserModel;
import com.example.mooc.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private static final Integer ALLOW_LOGIN_ATTEMPTS = 5;

    public UserModel loadUserByPrincipalAndApplyLoginAttempts(String email) {
         var user = userRepository.findByEmail(email);
         if (user.getLoginAttempts() >= ALLOW_LOGIN_ATTEMPTS) {
              throw new AuthExceedLoginAttemptsException();
         }
         userRepository.incrementLoginAttempts(email);
         return user;
    }

    public boolean resetLoginAttempts(String email) {
        return userRepository.resetLoginAttempts(email);
    }


}