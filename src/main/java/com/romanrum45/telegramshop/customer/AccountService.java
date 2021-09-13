package com.romanrum45.telegramshop.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

import java.time.LocalDateTime;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;


    public String getBalance(String chatId) {
        return this.accountRepository.findById(chatId)
                .map(accountEntity -> String.format(Locale.ENGLISH, "%.2f", accountEntity.getBalance()))
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


    /**
     * amount может быть как положительным числом, так и отрицательным в зависимости от операции.
     * @param chatId идентификатор чата пользователя.
     * @param amount сумма списани/зачисления.
     */
    public synchronized void updateBalance(String chatId, double amount) {
        var account = this.accountRepository.findById(chatId).orElseThrow();
        var currentBalance = account.getBalance();
        account.setBalance(currentBalance + amount);
        this.accountRepository.save(account);
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
