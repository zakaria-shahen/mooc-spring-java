package com.example.mooc.service;

import com.example.mooc.exception.OtpInvalidException;
import com.example.mooc.model.OtpModel;
import com.example.mooc.repository.OtpRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@AllArgsConstructor
@Service
public class OtpService {

    private OtpRepository otpRepository;

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public OtpModel generateAndSaveOtp(String receiver, OtpModel.OtpType otpType) {
        var otp = generateRandomOtpNumber();
        var otpModel = OtpModel.builder()
                .otp(passwordEncoder.encode(otp))
                .receiver(receiver)
                .type(otpType.name())
                .createAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                .build();
        otpModel = otpRepository.create(otpModel);
        otpModel.setOtp(otp);

        return otpModel;
    }

    private String generateRandomOtpNumber() {
        return String.valueOf(new SecureRandom().nextInt(9999));
    }

    public void validateOtpAndIncrementAttempts(Long id, String otp) {
        var otpModel = otpRepository.findById(id);
        if (otpModel.isUsed() || otpModel.getCreateAt() + otpModel.getExpirationAfterSeconds() < LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) {
            throw new OtpInvalidException();
        }

        if (!passwordEncoder.matches(otp, otpModel.getOtp())) {
            otpRepository.incrementAttemptsById(id);
            throw new OtpInvalidException();
        }
        otpRepository.updateOtpToUsedById(id);
    }


}
