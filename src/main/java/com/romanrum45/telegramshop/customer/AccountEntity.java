package com.romanrum45.telegramshop.customer;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "customerAccount")
@Data
public class AccountEntity {

    @Id
    private String chatId;

    // Баланс в рублях
    private int balance;

    // Потраченная сумма
    private int deposits;

    private String nickname;

    private LocalDateTime registrationTime;



}
