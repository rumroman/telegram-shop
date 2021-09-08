package com.romanrum45.telegramshop.bot;

import com.romanrum45.telegramshop.command.handler.CommandHandler;
import com.romanrum45.telegramshop.customer.AccountEntity;
import com.romanrum45.telegramshop.customer.AccountService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NikitaBot extends TelegramLongPollingBot {

    private final String name;
    private final String token;

    private final List<CommandHandler> commandHandlers;
    private final CommandHandler mainHandler;
    private final AccountService accountService;

    private static final Set<String> chatIdCache = new LinkedHashSet<>();

    public NikitaBot (String name, String token, List<CommandHandler> commandHandlers, CommandHandler mainHandler,
                      AccountService accountService) {
        this.name = name;
        this.token = token;
        this.commandHandlers = commandHandlers;
        this.mainHandler = mainHandler;
        this.accountService = accountService;
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            var chatId = update.getMessage().getChatId().toString();

            if (!chatIdCache.contains(chatId)) {
                if (!this.accountService.exists(chatId)) {
                    this.accountService.createAccount(chatId, update.getMessage().getChat().getUserName());
                }
                chatIdCache.add(chatId);
            }
        }

        if(update.hasMessage() && update.getMessage().hasText()) {
            var command = update.getMessage().getText().trim();
            var sendMessage = this.commandHandlers.stream()
                    .filter(h -> h.isCommandSupported(command))
                    .findFirst().orElse(mainHandler)
                    .handle(update);

            try {
                this.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
