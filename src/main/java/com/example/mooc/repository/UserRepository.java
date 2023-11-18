package com.example.mooc.repository;

import com.example.mooc.model.UserModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface UserRepository {

    UserModel findByEmail(@NotBlank @Email String email);

    UserModel findByEmailAndPassword(@NotBlank @Email String email, @NotBlank String password);

}
