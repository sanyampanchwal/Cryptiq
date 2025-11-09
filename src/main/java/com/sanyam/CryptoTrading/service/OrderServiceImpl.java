package com.sanyam.CryptoTrading.service;

import com.sanyam.CryptoTrading.domain.OrderStatus;
import com.sanyam.CryptoTrading.domain.OrderType;
import com.sanyam.CryptoTrading.model.*;
import com.sanyam.CryptoTrading.repository.OrderItemRepo;
import com.sanyam.CryptoTrading.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private WalletService walletService;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private AssetService assetService;


    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
        double price = orderItem.getCoin().getCurrentPrice()*orderItem.getQuantity();

        Order order = new Order();
        order.setUser(user);
        order.setOrderType(orderType);
        order.setOrderItem(orderItem);
        order.setPrice(BigDecimal.valueOf(price));
        order.setTimestamp(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        return orderRepo.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) throws Exception {
        return orderRepo.findById(orderId)
                .orElseThrow(
                        ()-> new Exception("Order not found"));

    }

    @Override
    public List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol) {
        return orderRepo.findByUserId(userId);
    }

    private OrderItem createOrderItem(Coin coin , double quantity ,
                                      double buyPrice , double sellPrice)
    {
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        return orderItem;
    }

    @Transactional //if exception like no balance , delete transaction from db
    public Order buyAsset(Coin coin , double quantity , User user) throws Exception {
        if(quantity <= 0){
            throw new Exception("Quantity should be >0 ");
        }
        double buyPrice = coin.getCurrentPrice();
        OrderItem orderItem = createOrderItem(coin , quantity , buyPrice ,0);
        Order order = createOrder(user,orderItem,OrderType.BUY);
        orderItem.setOrder(order);

        walletService.payOrderPayment(order,user);

        order.setOrderStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        Order saveOrder = orderRepo.save(order);

        //create asset ;
        Asset oldAsset = assetService.findAssetByUserIdAndCoinId(order.getUser().getId(),
                order.getOrderItem().getCoin().getId());

        if(oldAsset == null)
        {
            assetService.createAsset(user,orderItem.getCoin(),
                    orderItem.getQuantity());
        }
        else{
            assetService.updateAsset(oldAsset.getId(),quantity);
        }

        return saveOrder;
    }


    @Transactional //if exception like no balance , delete transaction from db
    public Order sellAsset(Coin coin , double quantity , User user)
            throws Exception {

        if(quantity <= 0){
            throw new Exception("Quantity should be >0 ");
        }
        Asset assetToSell = assetService
                .findAssetByUserIdAndCoinId
                        (user.getId(), coin.getId());

        double sellPrice = coin.getCurrentPrice();

        if(assetToSell != null) {

            double buyPrice = assetToSell.getBuyPrice();
            OrderItem orderItem = createOrderItem(coin, quantity, 0, sellPrice);
            Order order = createOrder(user, orderItem, OrderType.SELL);
            orderItem.setOrder(order);

            if (assetToSell.getQuantity() >= quantity) {
                order.setOrderStatus(OrderStatus.SUCCESS);
                order.setOrderType(OrderType.SELL);
                Order saveOrder = orderRepo.save(order);

                walletService.payOrderPayment(order, user);
                Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), -quantity);
                if (updatedAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
                    assetService.deleteAsset(updatedAsset.getId());
                }
                return saveOrder;
            }
            throw new Exception("Insufficient quantity ");
        }
        throw new Exception("Asset not found ");
    }


    @Override
    @Transactional
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {

        if(orderType == OrderType.BUY)
        {
            return buyAsset(coin,quantity,user);
        }
        else if(orderType.equals(OrderType.SELL))
        {
            return sellAsset(coin,quantity,user);
        }
        throw new Exception("Invalid Order Type");

    }
}
