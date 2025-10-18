package com.sanyam.CryptoTrading.repository;

import com.sanyam.CryptoTrading.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User,Long> {
    User findByEmail(String email);
}
