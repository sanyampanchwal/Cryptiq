package com.sanyam.CryptoTrading.repository;

import com.sanyam.CryptoTrading.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepo extends JpaRepository<PaymentOrder, Long> {

}
