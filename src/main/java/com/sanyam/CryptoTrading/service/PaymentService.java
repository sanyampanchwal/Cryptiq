package com.sanyam.CryptoTrading.service;

import com.razorpay.RazorpayException;
import com.sanyam.CryptoTrading.domain.PaymentMethod;
import com.sanyam.CryptoTrading.model.PaymentOrder;
import com.sanyam.CryptoTrading.model.User;
import com.sanyam.CryptoTrading.response.PaymentResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {
    PaymentOrder createOrder(User user, Long amount,
                             PaymentMethod paymentMethod);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

    PaymentResponse createRazorpayPaymentLink(User user, Long amount) throws RazorpayException;

    PaymentResponse createStripePaymentLink(User user, Long amount,Long orderId) throws StripeException;
}
