package com.example.mooc.controller;

import com.example.mooc.dto.UserDto;
import com.example.mooc.security.UserService;
import com.example.mooc.utils.AuthorizationUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @GetMapping("{id}")
    public UserDto fetch(@PathVariable(required = false) Long id, JwtAuthenticationToken principle) {
        id = (id != null && AuthorizationUtils.isAdmin(principle)) ? id : AuthorizationUtils.getUserId(principle);
        return userService.fetchById(id);
    }

    @PutMapping("{id}")
    public UserDto update(@PathVariable(required = false) Long id, @RequestBody UserDto userDto, JwtAuthenticationToken principle) {
        if (AuthorizationUtils.isAdmin(principle)) {
            id = id != null ? id : AuthorizationUtils.getUserId(principle);
            userDto.setId(id);
        } else {
            userDto.setId(AuthorizationUtils.getUserId(principle));
        }
        return userService.updateUser(userDto);
    }
}
