package com.romanrum45.telegramshop;

import com.romanrum45.telegramshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.romanrum45.telegramshop.TextConstants.*;

@RequiredArgsConstructor
public class NikitaBot extends TelegramLongPollingBot {

    private final String name;
    private final String token;

    private final ResourceBundle resourceBundle;

    private final ProductService ozonService;

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
        if(update.hasMessage() && update.getMessage().hasText()) {
            var receiveMessage = update.getMessage().getText().trim();
            var chatId = update.getMessage().getChatId();
            var message = update.getMessage();
            var sendMessage = new SendMessage();


            switch (receiveMessage) {
                case BUY_COMMAND:
                    this.handleBuyCommand(message);
                    return;
                case GOODS_COMMAND:
                    return;
                case ACCOUNT_COMMAND:
                    return;
                case BALANCE_COMMAND:
                    return;
                case HELP_COMMAND:
                    return;
                case RULES_COMMAND:
                    return;
                case MAIN_COMMAND:
                    return;
                default:
                    break;
            }

            sendMessage.setReplyMarkup(this.getMainMenuKeyboard());
            sendMessage.setChatId(chatId.toString());
            sendMessage.setText(this.resourceBundle.getString("use-keyboard"));


//            var sendDocument = new SendDocument();
//            sendDocument.setChatId(chatId.toString());
//            sendDocument.setDocument();


            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleBuyCommand(Message message) {
        var sendMessage = new SendMessage();
        var listRow = new ArrayList<KeyboardRow>();
        if (!this.ozonService.getProductsInStock().isEmpty()) {
            var row = new KeyboardRow();
            row.add(this.ozonService.getNameProduct());
            var replyKeyboard = new ReplyKeyboardMarkup(listRow);
            replyKeyboard.setResizeKeyboard(true);
            sendMessage.setReplyMarkup(replyKeyboard);
            sendMessage.setText(this.resourceBundle.getString("buy"));
        } else {
            sendMessage.setText(this.resourceBundle.getString("no-products"));
        }

        sendMessage.setChatId(message.getChatId().toString());

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void receiveOzonFile(Message message) {

    }



    private ReplyKeyboardMarkup getMainMenuKeyboard() {
        var listRow = new ArrayList<KeyboardRow>();
        var row = new KeyboardRow();
        row.add("Купить");
        row.add("Наличие товаров");
        listRow.add(row);
        row = new KeyboardRow();
        row.add("Личный кабинет");
        row.add("Баланс");
        listRow.add(row);
        row = new KeyboardRow();
        row.add("Помощь");
        row.add("Правила");
        row.add("О боте");
        listRow.add(row);
        var replyKeyboard = new ReplyKeyboardMarkup(listRow);
        replyKeyboard.setResizeKeyboard(true);
        return replyKeyboard;
    }
}
