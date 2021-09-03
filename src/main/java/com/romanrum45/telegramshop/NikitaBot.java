package com.romanrum45.telegramshop;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

import static com.romanrum45.telegramshop.TextConstants.*;

@RequiredArgsConstructor
public class NikitaBot extends TelegramLongPollingBot {



    private final String name;
    private final String token;

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
            var sendMessage = new SendMessage();

            switch (receiveMessage) {
                case BUY_COMMAND:
                    break;
                case GOODS_COMMAND:
                    break;
                case ACCOUNT_COMMAND:
                    break;
                case BALANCE_COMMAND:
                    break;
                case HELP_COMMAND:
                    break;
                case RULES_COMMAND:
                    break;
                case MAIN_COMMAND:
                    break;
                default:
                    break;
            }

            sendMessage.setReplyMarkup(this.getMainMenuKeyboard());
            sendMessage.setChatId(chatId.toString());
            sendMessage.setText("${use-keyboard}");

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
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
