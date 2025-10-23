package com.sanyam.CryptoTrading.service;

import com.sanyam.CryptoTrading.domain.VerificationType;
import com.sanyam.CryptoTrading.model.ForgotPasswordToken;
import com.sanyam.CryptoTrading.model.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user ,
                                    String id ,String otp,
                                    VerificationType verificationType,
                                    String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);
}
