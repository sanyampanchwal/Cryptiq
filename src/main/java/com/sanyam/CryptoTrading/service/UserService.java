package com.sanyam.CryptoTrading.service;

import com.sanyam.CryptoTrading.domain.VerificationType;
import com.sanyam.CryptoTrading.model.User;

public interface UserService {
    public User findUserByJwt(String jwt) throws Exception;
    public User findUserByEmail(String email) throws Exception;
    public User findUserById(long userId) throws Exception;

    public User enableTwoFactorAuth(VerificationType verificationType
            ,String sendTo
            , User user);

    public User updatePassword(User user, String newPassword) throws Exception;
}
