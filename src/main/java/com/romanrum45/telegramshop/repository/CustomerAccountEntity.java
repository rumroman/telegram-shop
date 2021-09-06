package com.romanrum45.telegramshop.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customerAccount")
public class CustomerAccountEntity {

    @Id
    private String chatId;

    // Баланс в рублях
    private int balance;



}
