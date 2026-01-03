package com.sanyam.CryptoTrading.model;


import com.sanyam.CryptoTrading.domain.OrderStatus;
import com.sanyam.CryptoTrading.domain.OrderType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @Column(nullable = false) //req field
    private OrderType orderType; //buy or sell

    @Column(nullable = false)
    private BigDecimal price;

    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(nullable = false)
    private OrderStatus orderStatus;

    @OneToOne(mappedBy = "order" ,cascade = CascadeType.ALL)
    private OrderItem orderItem;


}
