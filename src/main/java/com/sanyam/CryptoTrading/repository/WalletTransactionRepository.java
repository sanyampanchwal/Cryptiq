package com.sanyam.CryptoTrading.repository;

import com.sanyam.CryptoTrading.model.Wallet;
import com.sanyam.CryptoTrading.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long> {

    List<WalletTransaction> findByWalletOrderByDateDesc(Wallet wallet);

}
