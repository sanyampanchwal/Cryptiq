package com.sanyam.CryptoTrading.service;

import com.sanyam.CryptoTrading.model.User;
import com.sanyam.CryptoTrading.model.Withdrawal;

import java.util.List;

public interface WithdrawalService {

    Withdrawal requestWithdrawal(Long amount , User user);

    Withdrawal proceedWithWithdrawal(Long WithdrawalId, boolean accept) throws Exception;

    List<Withdrawal> getUsersWithdrawalHistory(User user);

    List<Withdrawal> getAllWithdrawalRequest();

}
