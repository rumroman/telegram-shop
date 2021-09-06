package com.romanrum45.telegramshop;

import com.romanrum45.telegramshop.repository.PaymentTransactionEntity;
import com.romanrum45.telegramshop.repository.PaymentTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;

@RestController
@Slf4j
public class TestController {

    private final PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    public TestController(PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
    }


    @GetMapping("/index")
    public String index() {
        return "Hello world";
    }

    @PostMapping("/notification")
    public String notification(@RequestParam String notification_type, @RequestParam String operation_id,
                               @RequestParam String amount, @RequestParam String currency,
                               @RequestParam String datetime, @RequestParam String sender,
                               @RequestParam String withdrawAmount, @RequestParam boolean unaccepted) {
        if (!currency.equals("643")) throw new IllegalArgumentException("Перевод в другой валюте");

        var paymentTransaction = PaymentTransactionEntity.builder()
                .operationId(operation_id)
                .notificationType(notification_type)
                .amount(amount)
                .withdrawAmount(withdrawAmount)
                .currency(currency)
                .dateTime(Instant.parse(datetime).atZone(ZoneId.systemDefault()).toLocalDateTime())
                .sender(sender)
                .unaccepted(unaccepted)
                .build();

        var savedPaymentTransaction = paymentTransactionRepository.save(paymentTransaction);

        log.debug("Notification transaction. Saved: {}", savedPaymentTransaction);

        return "";
    }
}
