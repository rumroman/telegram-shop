package com.romanrum45.telegramshop.bot;

import com.romanrum45.telegramshop.command.handler.CommandHandler;
import com.romanrum45.telegramshop.customer.AccountEntity;
import com.romanrum45.telegramshop.customer.AccountService;
import com.romanrum45.telegramshop.service.ozon.OzonProductRepository;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NikitaBot extends TelegramLongPollingBot {

    private final String name;
    private final String token;
    private final String userId;
    private final String secretCaption;

    private final List<CommandHandler> commandHandlers;
    private final CommandHandler mainHandler;
    private final AccountService accountService;

    private static final Set<String> chatIdCache = new LinkedHashSet<>();

    public NikitaBot (String name, String token, List<CommandHandler> commandHandlers, CommandHandler mainHandler,
                      AccountService accountService, String userId, String secretCaption) {
        this.name = name;
        this.token = token;
        this.commandHandlers = commandHandlers;
        this.mainHandler = mainHandler;
        this.accountService = accountService;
        this.userId = userId;
        this.secretCaption = secretCaption;

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
            var message = update.getMessage();

            if (message.hasDocument()) {
                var document = message.getDocument();
                var caption = message.getCaption();
                if (caption != null && caption.equals(this.secretCaption)) {
                    var fileName = document.getFileName();
                    var getFile = new GetFile();
                    getFile.setFileId(document.getFileId());
                    try {
                        var file = execute(getFile);
//                        Files.move()
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        if(update.hasMessage() && update.getMessage().hasText()) {
            var command = update.getMessage().getText().trim();
            var handler = this.commandHandlers.stream()
                    .filter(h -> h.isCommandSupported(command))
                    .findFirst().orElse(mainHandler);

            var method = handler.handle(update);

            try {
                if (method.getClass() == SendMessage.class) {
                    this.execute((SendMessage) method);
                } else if (method.getClass() == SendDocument.class) {
                    var sendDocument = (SendDocument) method;
                    this.execute(sendDocument);
                    Files.delete(sendDocument.getDocument().getNewMediaFile().toPath());
                    this.execute(handler.getSupportText(update.getMessage().getChatId().toString()));
                    this.execute(handler.notificationSupportUser("Купили файл :  " + sendDocument.getDocument().getNewMediaFile().getName() + " Пользователь: " + update.getMessage().getChat().getUserName(), userId));
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
