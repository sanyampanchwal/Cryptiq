package com.sanyam.CryptoTrading.service;

import com.sanyam.CryptoTrading.model.Wallet;
import com.sanyam.CryptoTrading.model.WalletTransaction;
import com.sanyam.CryptoTrading.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Override
    public List<WalletTransaction> getTransactionsByWallet(Wallet wallet) {
        // Changed to use the method name that already exists in your Repository
        return walletTransactionRepository.findByWalletOrderByDateDesc(wallet);
    }
}