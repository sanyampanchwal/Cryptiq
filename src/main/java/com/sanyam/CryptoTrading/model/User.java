package com.sanyam.CryptoTrading.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sanyam.CryptoTrading.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  //provides automatic id to users
    private Long id;
    private String fullName;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //password will only be writable by user
    private String password;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;   //default role =customer
}
