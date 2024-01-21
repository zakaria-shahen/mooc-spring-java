package com.example.mooc.security;

import com.example.mooc.client.EmailClient;
import com.example.mooc.dto.request.LoginRequest;
import com.example.mooc.dto.request.RegistrationRequest;
import com.example.mooc.dto.request.RestCredentialsRequest;
import com.example.mooc.dto.request.SetCredentialsRequest;
import com.example.mooc.dto.response.LoginResponse;
import com.example.mooc.exception.AuthInvalidException;
import com.example.mooc.model.AccessTokenBlockList;
import com.example.mooc.model.OtpModel;
import com.example.mooc.model.RefreshTokenBlockList;
import com.example.mooc.model.UserModel;
import com.example.mooc.repository.AccessTokenBlockListRepo;
import com.example.mooc.repository.RefreshTokenBlockListRepository;
import com.example.mooc.service.OtpService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.PrematureJwtException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import static com.example.mooc.security.JweService.EXPIRATION_AFTER_SECONDS;

@Component
@AllArgsConstructor
public class AuthenticationService {
    private UserService userService;
    private AccessTokenBlockListRepo accessTokenBlockListRepo;
    private RefreshTokenBlockListRepository refreshTokenBlockListRepository;
    private EmailClient emailClient;
    private OtpService otpService;

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public LoginResponse authenticate(LoginRequest loginRequest) throws AuthenticationException {
        UserModel user = userService.loadUserByPrincipalAndApplyLoginAttempts(loginRequest.principal());

        if (!user.isStatus()) {
             throw new AuthInvalidException();
        }
        
        validateCredentials(loginRequest.credentials(), user.getPassword());
        user.setPassword(null);
        userService.resetLoginAttempts(user.getEmail());

        return new LoginResponse(
                JweService.generateAccessToken(user),
                JweService.generateRefreshToken(user),
                OAuth2AccessToken.TokenType.BEARER,
                EXPIRATION_AFTER_SECONDS
        );

    }

    private void validateCredentials(String credentials, String encryptedCredentials) {
         if (!passwordEncoder.matches(credentials, encryptedCredentials)) {
             throw new AuthInvalidException();
         }
    }

    public void userRegistration(RegistrationRequest registrationRequest, UserModel.ROLE role) {
        String password = passwordEncoder.encode(registrationRequest.password());
        userService.addUser(UserModel.builder()
                .email(registrationRequest.email())
                .name(registrationRequest.name())
                .role(role)
                .loginAttempts(0)
                .status(true)
                .password(password)
                .build()
        );
    }

    public LoginResponse refreshTokenGrantType(String refreshToken) {
        if (refreshTokenBlockListRepository.existsById(refreshToken)) {
             throw new AuthInvalidException();
        }
        var refreshTokenclaims = JweService.validateRefreshTokenAndGetClaims(refreshToken);
        refreshTokenBlockListRepository.save(new RefreshTokenBlockList(refreshToken));
        return new LoginResponse(
                JweService.generateAccessToken(refreshTokenclaims),
                JweService.generateRefreshToken(refreshTokenclaims),
                OAuth2AccessToken.TokenType.BEARER,
                EXPIRATION_AFTER_SECONDS
        );
    }

    public void logout(String accessToken, long userIdInAccessToken, String refreshToken) {
        Claims refreshTokenClaims;
        try {
            refreshTokenClaims = JweService.validateRefreshTokenAndGetClaims(refreshToken);
        } catch (PrematureJwtException ex) {
             if (!ex.getMessage().contains("JWT early by")) {
                 throw ex;
             }
             refreshTokenClaims = ex.getClaims();
        }

        if (refreshTokenClaims.get(JweService.ClaimNames.USER_ID, Long.class) != userIdInAccessToken) {
            throw new AuthInvalidException();
        }

        accessTokenBlockListRepo.save(new AccessTokenBlockList(accessToken));
        refreshTokenBlockListRepository.save(new RefreshTokenBlockList(refreshToken));
    }

    public void restCredentials(RestCredentialsRequest restCredentialsRequest) {
        var user = userService.loadUserByPrincipal(restCredentialsRequest.receiver());
        var otpModel = otpService.generateAndSaveOtp(restCredentialsRequest.receiver(), OtpModel.OtpType.RESET_CREDENTIALS);
        String body = STR."""
                Hello, if you need to reset your password, please use next id and otp to set new password.
                ID: \{otpModel.getId()}
                OTP: \{otpModel.getOtp()}
                """.strip();

        emailClient.send(restCredentialsRequest.receiver(), body);
    }

    public void setCredentials(SetCredentialsRequest setCredentialsRequest) {
        otpService.validateOtpAndIncrementAttempts(setCredentialsRequest.otpId(), setCredentialsRequest.otp());
        UserModel user = userService.loadUserByPrincipal(setCredentialsRequest.receiver());
        user.setPassword(passwordEncoder.encode(setCredentialsRequest.credentials()));
        userService.updateUser(user);
    }
}
