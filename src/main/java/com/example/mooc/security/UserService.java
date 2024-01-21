package com.example.mooc.security;

import com.example.mooc.dto.UserDto;
import com.example.mooc.exception.AuthExceedLoginAttemptsException;
import com.example.mooc.mapping.UserModelToDtoMapper;
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
         var user = loadUserByPrincipal(email);
         if (user.getLoginAttempts() >= ALLOW_LOGIN_ATTEMPTS) {
              throw new AuthExceedLoginAttemptsException();
         }
         userRepository.incrementLoginAttempts(email);
         return user;
    }

    public UserModel loadUserByPrincipal(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean resetLoginAttempts(String email) {
        return userRepository.resetLoginAttempts(email);
    }

    public UserModel addUser(UserModel userModel) {
        return userRepository.addUser(userModel);
    }


    public UserModel updateUser(UserModel userModel) {
        return userRepository.updateUser(userModel);
    }

    public boolean updateUserBasicInfo(UserDto userDto) {
        var model = UserModelToDtoMapper.INSTANCE.toModel(userDto);
        return userRepository.updateUserBasicInfo(model);
    }

    public UserDto fetchById(Long id) {
        var model = userRepository.findById(id);
        return UserModelToDtoMapper.INSTANCE.toDto(model);
    }
}
