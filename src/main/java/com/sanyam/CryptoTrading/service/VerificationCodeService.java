package com.sanyam.CryptoTrading.service;

import com.sanyam.CryptoTrading.domain.VerificationType;
import com.sanyam.CryptoTrading.model.User;
import com.sanyam.CryptoTrading.model.VerificationCode;

public interface VerificationCodeService {
    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId);


    void deleteVerificationCodeById(VerificationCode verificationCode);
}

