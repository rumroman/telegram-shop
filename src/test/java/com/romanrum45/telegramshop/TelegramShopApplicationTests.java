package com.romanrum45.telegramshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class TelegramShopApplicationTests {

    @Test
    void contextLoads() {

        double amount = -35.24;
        double balance = 100.00;

        System.out.println("Итого: " + (balance + amount));
    }

}
