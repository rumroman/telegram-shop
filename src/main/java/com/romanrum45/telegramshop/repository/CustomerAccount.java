package com.romanrum45.telegramshop.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "customerAccount")
public class CustomerAccount {

    @Id
    private String chatId;

    // Баланс в рублях
    private int balance;

}
