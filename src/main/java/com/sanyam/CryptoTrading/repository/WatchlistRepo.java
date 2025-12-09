package com.sanyam.CryptoTrading.repository;

import com.sanyam.CryptoTrading.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchlistRepo extends JpaRepository<Watchlist, Long> {
    Watchlist findByUserId(Long UserId);

}
