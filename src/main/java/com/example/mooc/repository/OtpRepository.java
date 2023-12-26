package com.example.mooc.repository;

import com.example.mooc.model.OtpModel;

public interface OtpRepository {

    OtpModel findById(Long id);

    OtpModel create(OtpModel otpModel);

    boolean incrementAttemptsById(Long id);

    boolean updateOtpToUsedById(Long id);
}
