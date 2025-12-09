package com.sanyam.CryptoTrading.service;

import com.sanyam.CryptoTrading.model.PaymentDetails;
import com.sanyam.CryptoTrading.model.User;

public interface PaymentDetailsService {
    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountHolderName,
                                            String ifsc,
                                            String bankName,
                                            User user);
    public PaymentDetails getUsersPaymentDetails(User user);
}
