package com.sanyam.CryptoTrading.request;


import com.sanyam.CryptoTrading.domain.OrderType;
import lombok.Data;

@Data
public class CreateOrderRequest {
    private String coinId;
    private double quantity;
    private OrderType orderType;
}
