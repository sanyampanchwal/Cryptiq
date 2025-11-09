package com.sanyam.CryptoTrading.repository;

import com.sanyam.CryptoTrading.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
}
