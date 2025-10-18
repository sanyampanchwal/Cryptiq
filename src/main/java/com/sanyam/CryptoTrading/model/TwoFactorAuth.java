package com.sanyam.CryptoTrading.model;

import com.sanyam.CryptoTrading.domain.VerificationType;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled = false;
    private VerificationType sendTo;

}
