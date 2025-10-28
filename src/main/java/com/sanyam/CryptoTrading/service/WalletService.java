package com.sanyam.CryptoTrading.service;

import com.sanyam.CryptoTrading.model.Order;
import com.sanyam.CryptoTrading.model.User;
import com.sanyam.CryptoTrading.model.Wallet;

import java.math.BigDecimal;

public interface WalletService {
    Wallet getUserWallet(User user);
    Wallet addBalance(Wallet walled , Long money);
    Wallet findWalletById(Long id) throws Exception;
    Wallet walletToWalletTransfer(User sender, Wallet recipientWallet, Long amount) throws Exception;
    Wallet payOrderPayment(Order order , User user) throws Exception;
}
