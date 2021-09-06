package com.romanrum45.telegramshop;

import com.romanrum45.telegramshop.customer.CustomerAccountRepository;
import com.romanrum45.telegramshop.customer.PaymentTransactionRepository;
import com.romanrum45.telegramshop.service.ProductService;
import com.romanrum45.telegramshop.service.ozon.FileOzonProductRepository;
import com.romanrum45.telegramshop.service.ozon.OzonProductRepository;
import com.romanrum45.telegramshop.service.ozon.OzonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceResourceBundle;

import java.nio.file.Path;

@Configuration
public class DefaultConfiguration {

    @Bean
    public NikitaBot nikitaBot(@Value("${bot.name}") String botName,
                               @Value("${bot.token}") String token,
                               ProductService ozonService) {
        return new NikitaBot(botName, token, MessageSourceResourceBundle.getBundle("messages"), ozonService);
    }

    @Bean
    public ProductService ozonService(PaymentTransactionRepository paymentTransactionRepository,
                                      CustomerAccountRepository customerAccountRepository,
                                      OzonProductRepository fileOzonProductRepository) {
        return new OzonService(paymentTransactionRepository, customerAccountRepository, fileOzonProductRepository);
    }


    @Bean(initMethod = "getProducts")
    public OzonProductRepository fileOzonProductRepository(@Value("${available-files-path}") String availableFilesPath,
                                                           @Value("${old-files-path}") String oldFilesPath) {
        var availableFiles = Path.of(availableFilesPath);
        var oldFiles = Path.of(oldFilesPath);

        return new FileOzonProductRepository(availableFiles, oldFiles);
    }

}
