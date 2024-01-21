package com.example.mooc.controller;

import com.example.mooc.dto.UserDto;
import com.example.mooc.security.UserService;
import com.example.mooc.utils.AuthorizationUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private AuthController authController;

    @GetMapping({"/{id}", ""})
    public UserDto fetch(@PathVariable(required = false) Long id, JwtAuthenticationToken principle) {
        id = (id == null || AuthorizationUtils.isNotAdmin(principle)) ? AuthorizationUtils.getUserId(principle) : id;
        return userService.fetchById(id);

    }

    @PutMapping({"{id}", ""})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @PathVariable(required = false) Long id,
            @RequestBody UserDto userDto,
            JwtAuthenticationToken principle,
            @RequestParam("refresh-token") String refreshToken
    ) {
        id = (id == null || AuthorizationUtils.isNotAdmin(principle)) ? AuthorizationUtils.getUserId(principle) : id;
        userDto.setId(id);
        userService.updateUserBasicInfo(userDto);
        authController.logout(refreshToken, principle);
    }
}

