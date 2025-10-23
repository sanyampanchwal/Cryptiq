package com.sanyam.CryptoTrading.request;

import com.sanyam.CryptoTrading.domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {
    private String sendTo;
    private VerificationType verificationType;
}
