package com.sanyam.CryptoTrading.repository;

import com.sanyam.CryptoTrading.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailsRepo extends JpaRepository<PaymentDetails, Long> {
    PaymentDetails findByUserId(long userId);

}
