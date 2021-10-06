package com.romanrum45.telegramshop;

import com.romanrum45.telegramshop.bot.NikitaBot;
import com.romanrum45.telegramshop.handler.command.CommandHandler;
import com.romanrum45.telegramshop.handler.command.MainCommandHandler;
import com.romanrum45.telegramshop.handler.command.OzonCommandHandler;
import com.romanrum45.telegramshop.handler.command.PaymentCommandHandler;
import com.romanrum45.telegramshop.customer.AccountRepository;
import com.romanrum45.telegramshop.customer.AccountService;
import com.romanrum45.telegramshop.customer.PaymentTransactionRepository;
import com.romanrum45.telegramshop.customer.PaymentTransactionService;
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
                               CommandHandler paymentCommandHandler,
                               CommandHandler ozonCommandHandler,
                               AccountService accountService,
                               @Value("${support-user-id}") String userId,
                               @Value("${support-secret-caption}") String secretCaption,
                               @Value ("${available-files-path}") String ozonFilePath) {

        var commandHandlers = List.of(mainCommandHandler, paymentCommandHandler, ozonCommandHandler);

        return new NikitaBot(botName, token, commandHandlers, mainCommandHandler, accountService, userId, secretCaption,
                ozonFilePath);
    }

    @Bean
    public TestController testController(PaymentTransactionService paymentTransactionService,
                                         @Value("${yandex-secret}") String notificationSecret) {
        return new TestController(paymentTransactionService, notificationSecret);
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

    @Bean
    public CommandHandler paymentCommandHandler(@Value("${yandex-money-account}") String yandexAccount) {
        return new PaymentCommandHandler(yandexAccount);
    }

    @Bean
    public CommandHandler ozonCommandHandler(OzonService ozonService, AccountService accountService,
                                             @Value("${ozon-id-300}") String _300Id,
                                             @Value("${ozon-id-600}") String _600Id,
                                             @Value("${ozon-id-900}") String _900Id) {
        return new OzonCommandHandler(ozonService, accountService, _300Id, _600Id, _900Id);
    }

    @Bean
    public PaymentTransactionService paymentTransactionService(AccountService accountService,
                                                               PaymentTransactionRepository paymentTransactionRepository,
                                                               NikitaBot nikitaBot) {
        return new PaymentTransactionService(accountService, paymentTransactionRepository, nikitaBot);
    }

}
