package com.romanrum45.telegramshop;

import com.romanrum45.telegramshop.bot.NikitaBot;
import com.romanrum45.telegramshop.command.handler.CommandHandler;
import com.romanrum45.telegramshop.command.handler.MainCommandHandler;
import com.romanrum45.telegramshop.customer.AccountRepository;
import com.romanrum45.telegramshop.customer.AccountService;
import com.romanrum45.telegramshop.service.ozon.FileOzonProductRepository;
import com.romanrum45.telegramshop.service.ozon.OzonProductRepository;
import com.romanrum45.telegramshop.service.ozon.OzonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.util.List;

@Configuration
public class DefaultConfiguration {

    @Bean
    public NikitaBot nikitaBot(@Value("${bot.name}") String botName,
                               @Value("${bot.token}") String token,
                               CommandHandler mainCommandHandler,
                               AccountService accountService) {

        var commandHandlers = List.of(mainCommandHandler);

        return new NikitaBot(botName, token, commandHandlers, mainCommandHandler, accountService);
    }

    @Bean
    public OzonService ozonService(OzonProductRepository fileOzonProductRepository,
                                   @Value("${ozon-name}") String name,
                                   @Value("${ozon-price-300}") int price300,
                                   @Value("${ozon-price-600}") int price600,
                                   @Value("${ozon-price-900}") int price900) {
        return new OzonService(fileOzonProductRepository, name, price300, price600, price900);
    }


    @Bean(initMethod = "getProducts")
    public OzonProductRepository fileOzonProductRepository(@Value("${available-files-path}") String availableFilesPath,
                                                           @Value("${old-files-path}") String oldFilesPath) {
        var availableFiles = Path.of(availableFilesPath);
        var oldFiles = Path.of(oldFilesPath);

        return new FileOzonProductRepository(availableFiles, oldFiles);
    }

    @Bean
    public AccountService accountService(AccountRepository accountRepository) {
        return new AccountService(accountRepository);
    }

    @Bean
    public CommandHandler mainCommandHandler(OzonService ozonService,
                                             AccountService accountService,
                                             @Value("${support-user-name}") String supportUserName) {
        return new MainCommandHandler(ozonService, accountService, supportUserName);
    }

}
