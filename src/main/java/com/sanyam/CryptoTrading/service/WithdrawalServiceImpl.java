package com.sanyam.CryptoTrading.service;


import com.sanyam.CryptoTrading.domain.WithdrawalStatus;
import com.sanyam.CryptoTrading.model.User;
import com.sanyam.CryptoTrading.model.Withdrawal;
import com.sanyam.CryptoTrading.repository.WithdrawalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    @Autowired
    private WithdrawalRepo withdrawalRepo;



    @Override
    public Withdrawal requestWithdrawal(Long amount, User user) {
        Withdrawal  withdrawal = new Withdrawal();
        withdrawal.setAmount(amount);
        withdrawal.setUser(user);

        withdrawal.setStatus(WithdrawalStatus.PENDING);
        return withdrawalRepo.save(withdrawal);
    }

    @Override
    public Withdrawal proceedWithWithdrawal(Long WithdrawalId, boolean accept) throws Exception {
        Optional<Withdrawal> withdrawal = withdrawalRepo.findById(WithdrawalId);
        if(withdrawal.isEmpty()) {
            throw new Exception("Withdrawal not found");
        }
        Withdrawal withdrawal1 = withdrawal.get();

        withdrawal1.setDate(LocalDateTime.now());
            if(accept) {
                withdrawal1.setStatus(WithdrawalStatus.SUCCESS);
            }
            else{
                withdrawal1.setStatus(WithdrawalStatus.DECLINED);
            }
        return withdrawalRepo.save(withdrawal1);
    }

    @Override
    public List<Withdrawal> getUsersWithdrawalHistory(User user) {
        return withdrawalRepo.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {
        return withdrawalRepo.findAll();
    }
}
