package com.sanyam.CryptoTrading.service;

import com.sanyam.CryptoTrading.model.TwoFactorOtp;
import com.sanyam.CryptoTrading.model.User;

public interface TwoFactorOtpService {
    TwoFactorOtp  createTwoFactorOtp(User user ,String otp , String jwt);
    TwoFactorOtp findByUser(Long userId);
    TwoFactorOtp findById(String id);
    boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp , String otp ); //otp from db , otp from user should be same
    void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp);
}
