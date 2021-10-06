package com.romanrum45.telegramshop.bot;

import com.romanrum45.telegramshop.handler.command.CommandHandler;
import com.romanrum45.telegramshop.customer.AccountService;
import com.romanrum45.telegramshop.handler.document.DocumentHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class NikitaBot extends TelegramLongPollingBot {

    private final String name;
    private final String token;
    private final String userId;
    private final String secretCaption;
    private final String ozonFilesPath;

    private final List<CommandHandler> commandHandlers;
    private final CommandHandler mainHandler;
    private final AccountService accountService;

    private static final Set<String> chatIdCache = new LinkedHashSet<>();

    public NikitaBot (String name, String token, List<CommandHandler> commandHandlers, CommandHandler mainHandler,
                      AccountService accountService, String userId, String secretCaption, String ozonFilesPath) {
        this.name = name;
        this.token = token;
        this.commandHandlers = commandHandlers;
        this.mainHandler = mainHandler;
        this.accountService = accountService;
        this.userId = userId;
        this.secretCaption = secretCaption;
        this.ozonFilesPath = ozonFilesPath;

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
        try {
            if (update.hasMessage()) {
                var message = update.getMessage();
                var chatId = message.getChatId().toString();

                if (!chatIdCache.contains(chatId)) {
                    if (!this.accountService.exists(chatId)) {
                        this.accountService.createAccount(chatId, message.getChat().getUserName());
                    }
                    chatIdCache.add(chatId);
                }
                if (message.hasText()) {
                    var command = message.getText().trim();
                    var handler = this.commandHandlers.stream()
                            .filter(h -> h.isCommandSupported(command))
                            .findFirst().orElse(mainHandler);

                    var method = handler.handle(update);


                    if (method.getClass() == SendMessage.class) {
                        this.execute((SendMessage) method);
                    } else if (method.getClass() == SendDocument.class) {
                        var sendDocument = (SendDocument) method;
                        this.execute(sendDocument);
                        Files.delete(sendDocument.getDocument().getNewMediaFile().toPath());
                        this.execute(handler.getSupportText(update.getMessage().getChatId().toString()));
                        this.execute(handler.notificationSupportUser("Купили аккаунт :  " + sendDocument.getDocument().getNewMediaFile().getName() + " Пользователь: " + update.getMessage().getChat().getUserName(), userId));
                    }

                } else if (message.hasDocument()) {
                    var document = message.getDocument();
                    var caption = message.getCaption();

                    if (caption != null && caption.equals(this.secretCaption)) {
                        var fileName = document.getFileName();
                        var getFile = new GetFile();
                        getFile.setFileId(document.getFileId());

                        if (!fileName.matches("[369]00_\\d+\\.json")) {
                            var sendMessage = new SendMessage(message.getChatId().toString(), "\uD83D\uDD34 Неверный формат имени файла");
                            execute(sendMessage);
                        } else if (Files.exists(Path.of(ozonFilesPath + "/" + fileName))) {
                            var sendMessage = new SendMessage(message.getChatId().toString(), "\uD83D\uDD34 Файл с таким именем уже существует");
                            execute(sendMessage);
                        } else {
                            var filePath = execute(getFile);
                            var outputFile = new File(ozonFilesPath + "/" + fileName);
                            downloadFile(filePath, outputFile);
                            var sendMessage = new SendMessage(message.getChatId().toString(), "\uD83D\uDFE2 Файл успешно загружен в бота");
                            execute(sendMessage);
                        }
                    } else {
                        var sendMessage = this.mainHandler.handle(update);
                        execute((SendMessage) sendMessage);
                    }
                }
            }
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
    }

}
