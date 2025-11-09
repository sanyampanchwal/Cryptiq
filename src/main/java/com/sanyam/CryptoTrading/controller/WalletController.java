package com.sanyam.CryptoTrading.controller;

import com.sanyam.CryptoTrading.model.Order;
import com.sanyam.CryptoTrading.model.User;
import com.sanyam.CryptoTrading.model.Wallet;
import com.sanyam.CryptoTrading.model.WalletTransaction;
import com.sanyam.CryptoTrading.service.OrderService;
import com.sanyam.CryptoTrading.service.UserService;
import com.sanyam.CryptoTrading.service.WalletService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/api/wallet")
    public ResponseEntity<Wallet> getUserWallet(
            @RequestHeader("Authorization")String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/wallet/${walletId}/transfer")
    public ResponseEntity<Wallet>walletToWalletTransfer(
            @RequestHeader("Authorization")String jwt,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction req
            )throws Exception {
        User senderUser = userService.findUserByJwt(jwt);
        Wallet receiverWallet = walletService.findWalletById(walletId);
        Wallet wallet = walletService.walletToWalletTransfer(senderUser ,
                receiverWallet ,
                req.getAmount());
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);

    }

    @PutMapping("/api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet>payOrderPayment(
            @RequestHeader("Authorization")String jwt,
            @PathVariable Long orderId
    )throws Exception {
        User user = userService.findUserByJwt(jwt);

        Order order = orderService.getOrderById(orderId);

        Wallet wallet = walletService.payOrderPayment(order,user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);

    }
}
