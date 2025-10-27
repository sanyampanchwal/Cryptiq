package com.sanyam.CryptoTrading.repository;

import com.sanyam.CryptoTrading.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepo extends JpaRepository<Coin, String> {

}
