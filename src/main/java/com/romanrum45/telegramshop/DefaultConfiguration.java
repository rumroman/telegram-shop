package com.romanrum45.telegramshop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceResourceBundle;

@Configuration
public class DefaultConfiguration {

    @Bean
    public NikitaBot nikitaBot(@Value("${bot.name}") String botName,
                               @Value("${bot.token}") String token) {
        return new NikitaBot(botName, token, MessageSourceResourceBundle.getBundle("messages"));
    }

}
