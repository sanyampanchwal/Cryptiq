package com.sanyam.CryptoTrading.service;

import com.sanyam.CryptoTrading.domain.OrderType;
import com.sanyam.CryptoTrading.model.Coin;
import com.sanyam.CryptoTrading.model.Order;
import com.sanyam.CryptoTrading.model.OrderItem;
import com.sanyam.CryptoTrading.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface OrderService {

    Order createOrder(User user , OrderItem orderItem , OrderType orderType) ;
    Order getOrderById(Long orderId) throws Exception;

    List<Order> getAllOrdersOfUser(Long userId , OrderType orderType, String assetSymbol) ;

    Order processOrder(Coin coin,double quantity, OrderType orderType, User user) throws Exception;

}
