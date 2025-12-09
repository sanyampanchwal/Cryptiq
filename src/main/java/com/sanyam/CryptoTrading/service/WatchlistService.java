package com.sanyam.CryptoTrading.service;

import com.sanyam.CryptoTrading.model.Coin;
import com.sanyam.CryptoTrading.model.User;
import com.sanyam.CryptoTrading.model.Watchlist;

public interface WatchlistService {
    Watchlist findUserWatchlist(Long userId) throws Exception;
    Watchlist createWatchlist(User user);

    Watchlist findById(Long id) throws Exception;

    Coin addItemToWatchlist(Coin coin, User user) throws Exception;


}
