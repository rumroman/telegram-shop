package com.romanrum45.telegramshop.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;


    public int getBalance(String chatId) {
        return this.accountRepository.findById(chatId)
                .map(AccountEntity::getBalance)
                .orElseThrow();
    }

    public int getDepositsSum(String chatId) {
        return this.accountRepository.findById(chatId)
                .map(AccountEntity::getDeposits)
                .orElseThrow();
    }

    public String getNickname(String chatId) {
        return this.accountRepository.findById(chatId)
                .map(AccountEntity::getNickname)
                .orElseThrow();
    }

    public boolean exists(String chatId) {
        return this.accountRepository.existsById(chatId);
    }

    public AccountEntity createAccount(String chatId, String username) {
        var newAccount = new AccountEntity();
        newAccount.setNickname(username);
        newAccount.setChatId(chatId);
        newAccount.setRegistrationTime(LocalDateTime.now());
        return this.accountRepository.save(newAccount);
    }
}
