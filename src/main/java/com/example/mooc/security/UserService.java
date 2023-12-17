package com.example.mooc.security;

import com.example.mooc.model.UserModel;
import com.example.mooc.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public UserModel loadUserByPrincipal(String email) {
        return userRepository.findByEmail(email);
    }





}
