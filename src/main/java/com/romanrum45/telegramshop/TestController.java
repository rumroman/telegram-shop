package com.romanrum45.telegramshop;

import com.romanrum45.telegramshop.customer.PaymentTransactionEntity;
import com.romanrum45.telegramshop.customer.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@ResponseBody
public class TestController {

    private final PaymentTransactionService paymentTransactionService;
    private final String notificationSecret;


    @GetMapping("/index")
    public String index() {
        return "Hello world";
    }

    @PostMapping("/notification")
    public String notification(@RequestParam String notification_type, @RequestParam String operation_id,
                               @RequestParam String amount, @RequestParam String currency,
                               @RequestParam String datetime, @RequestParam String sender,
                               @RequestParam(defaultValue = "false") boolean unaccepted, @RequestParam String label,
                               @RequestParam String sha1_hash, @RequestParam boolean codepro) {
        if (this.validateHash(notification_type, operation_id, amount, currency, datetime, sender,
                codepro, label, sha1_hash)) {
            if (!currency.equals("643")) throw new IllegalArgumentException("Перевод в другой валюте");

            var paymentTransaction = PaymentTransactionEntity.builder()
                    .chatId(label)
                    .operationId(operation_id)
                    .notificationType(notification_type)
                    .amount(amount)
                    .currency(currency)
                    .dateTime(Instant.parse(datetime).atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .sender(sender)
                    .unaccepted(unaccepted)
                    .build();

            this.paymentTransactionService.paymentReceived(paymentTransaction);
        }

        return "";
    }

    private boolean validateHash(String notification_type, String operation_id, String amount,
                                 String currency, String datetime, String sender, boolean codepro,
                                 String label, String sha1_hash) {
        var value = notification_type+"&"+operation_id+"&"+amount+"&"+currency+"&"+
                datetime+"&"+sender+"&"+codepro+"&"+notificationSecret+"&"+label;

        try {
            var md = MessageDigest.getInstance("SHA-1");
            var key = value.getBytes(StandardCharsets.UTF_8);
            var hash = md.digest(key);
            StringBuilder result = new StringBuilder();
            for (byte b : hash) {
                result.append(String.format("%02x", b));
            }

            if (result.toString().equals(sha1_hash)) {
                return true;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return false;
    }
}
