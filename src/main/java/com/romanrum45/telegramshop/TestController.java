package com.romanrum45.telegramshop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@RestController
@Slf4j
public class TestController {


    @GetMapping("/index")
    public String index() {
        return "Hello world";
    }

    @PostMapping("/notification")
    public String notification(@RequestParam String notification_type, @RequestParam String operation_id,
                               @RequestParam String amount, @RequestParam String currency,
                               @RequestParam String datetime, @RequestParam String sender) {
        if (!currency.equals("643")) throw new IllegalArgumentException("Перевод в другой валюте");

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        log.info("sender: {}", sender);
        log.info("instant: {}", Instant.parse(datetime).atZone(ZoneId.systemDefault()).toInstant());
        log.info("type: {}", notification_type);
        try {
            log.info("datetime: {}", inputFormat.parse(datetime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
