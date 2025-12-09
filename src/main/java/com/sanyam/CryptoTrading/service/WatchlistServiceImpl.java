package com.sanyam.CryptoTrading.service;

import com.sanyam.CryptoTrading.model.Coin;
import com.sanyam.CryptoTrading.model.User;
import com.sanyam.CryptoTrading.model.Watchlist;
import com.sanyam.CryptoTrading.repository.WatchlistRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchlistServiceImpl implements WatchlistService {

    @Autowired
    WatchlistRepo watchlistRepo;

    @Override
    public Watchlist findUserWatchlist(Long userId) throws Exception {
        Watchlist watchlist = watchlistRepo.findByUserId(userId);

        if(watchlist == null) {
            throw new Exception("Watchlist not found");
        }
        return watchlist;
    }

    @Override
    public Watchlist createWatchlist(User user) {
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        return watchlistRepo.save(watchlist);
    }

    @Override
    public Watchlist findById(Long id) throws Exception {
        Optional<Watchlist> watchlistOptional = watchlistRepo.findById(id);
        if(watchlistOptional.isEmpty()) {
            throw new Exception("Watchlist not Found");
        }
        return watchlistOptional.get();
    }

    @Override
    public Coin addItemToWatchlist(Coin coin, User user) throws Exception {
        Watchlist watchlist  = findUserWatchlist(user.getId());
        if(watchlist.getCoins().contains(coin)) {
            watchlist.getCoins().remove(coin);
        }
        else watchlist.getCoins().add(coin);
        watchlistRepo.save(watchlist);
        return coin;
    }
}
