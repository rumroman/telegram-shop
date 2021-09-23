package com.romanrum45.telegramshop.customer;

import com.romanrum45.telegramshop.bot.NikitaBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RequiredArgsConstructor
@Slf4j
public class PaymentTransactionService {


    private final AccountService accountService;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final NikitaBot nikitaBot;


    public void paymentReceived(PaymentTransactionEntity paymentTransactionEntity) {
        if (this.paymentTransactionRepository.existsById(paymentTransactionEntity.getOperationId())) {
            log.info("Transaction: the operationId {} already exists!", paymentTransactionEntity.getOperationId());
        } else {
            this.paymentTransactionRepository.save(paymentTransactionEntity);
            this.accountService.updateBalance(paymentTransactionEntity.getChatId(),
                    Double.parseDouble(paymentTransactionEntity.getAmount()));
            var sendMessage = new SendMessage();
            sendMessage.setChatId(paymentTransactionEntity.getChatId());
            sendMessage.setText("Баланс пополнен на " + paymentTransactionEntity.getAmount() + " руб.");

            try {
                nikitaBot.execute(sendMessage);
                log.info("Transaction saved: {}", paymentTransactionEntity);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

}
