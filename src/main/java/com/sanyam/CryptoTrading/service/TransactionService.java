package com.sanyam.CryptoTrading.service;

import com.sanyam.CryptoTrading.model.Wallet;
import com.sanyam.CryptoTrading.model.WalletTransaction;
import java.util.List;

public interface TransactionService {
    List<WalletTransaction> getTransactionsByWallet(Wallet wallet);
}