package com.romanrum45.telegramshop.command.handler;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
public class PaymentCommandHandler implements CommandHandler {

    private final static String UP_BALANCE_COMMAND = "Пополнить баланс";
    private final static String YANDEX_MONEY_COMMAND = "Яндекс деньги";

    private final static String CHOOSE_PAYMENT_METHOD_MESSAGE = "Выберите метод оплаты";
    private final static String ENTER_AMOUNT_MESSAGE = "Введите сумму, на которую хотите пополнить бота, не менее 2 рублей.";

    private final static String MAIN_MENU_ROW = "Главное меню";

    private final static String PAY_30_BUTTON = "Пополнить на 30р.";
    private final static String PAY_50_BUTTON = "Пополнить на 50р.";
    private final static String PAY_100_BUTTON = "Пополнить на 100р.";
    private final static String PAY_250_BUTTON = "Пополнить на 250р.";
    private final static String PAY_500_BUTTON = "Пополнить на 500р.";
    private final static String PAY_1000_BUTTON = "Пополнить на 1000р.";

    private final List<String> commands;

    private final String yandexAccount;

    public PaymentCommandHandler(String yandexAccount) {
        this.yandexAccount = yandexAccount;

        this.commands = List.of(UP_BALANCE_COMMAND, YANDEX_MONEY_COMMAND);
    }


    @Override
    public SendMessage handle(Update update) {
        var receivedMessage = update.getMessage().getText().trim();
        var message = update.getMessage();
        switch (receivedMessage) {
            case UP_BALANCE_COMMAND:
                return this.handleUpBalanceCommand(message);
            case YANDEX_MONEY_COMMAND:
                return this.handleYandexMoneyCommand(message);
            default:
                throw new UnsupportedOperationException();
        }
    }

    private SendMessage handleYandexMoneyCommand(Message receivedMessage) {
        var chatId = receivedMessage.getChatId().toString();
        var listRow = new ArrayList<KeyboardRow>();
        var mainRow = new KeyboardRow();
        mainRow.add(MAIN_MENU_ROW);
        listRow.add(mainRow);
        var replyKeyboard = new ReplyKeyboardMarkup(listRow);
        replyKeyboard.setResizeKeyboard(true);


        var inlineKeyboard = new InlineKeyboardMarkup();
        var pay30Btn = new InlineKeyboardButton();
        pay30Btn.setText(PAY_30_BUTTON);
        var sum = String.format("%4f", 30 / 0.98);
        var url = String.format(Locale.ENGLISH, "https://money.yandex.ru/quickpay/confirm.xml?receiver=%s&formcomment=Пополнение+баланса+в+боте&label=%s&quickpay-form=donate&targets=Пополнение+баланса+для+аккаунта+%s&sum=%s",
                this.yandexAccount, chatId, chatId, sum);
        pay30Btn.setUrl(url);

        var pay50Btn = new InlineKeyboardButton();
        pay50Btn.setText(PAY_30_BUTTON);
        var sum50 = String.format("%4f", 50 / 0.98);
        var url50 = String.format(Locale.ENGLISH, "https://money.yandex.ru/quickpay/confirm.xml?receiver=%s&formcomment=Пополнение+баланса+в+боте&label=%s&quickpay-form=donate&targets=Пополнение+баланса+для+аккаунта+%s&sum=%s",
                this.yandexAccount, chatId, chatId, sum50);
        pay50Btn.setUrl(url50);

        inlineKeyboard.setKeyboard();


        return this.getSendMessage(replyKeyboard, ENTER_AMOUNT_MESSAGE, chatId);
    }

    private SendMessage handleUpBalanceCommand(Message receivedMessage) {
        var listRow = new ArrayList<KeyboardRow>();
        var yandexRow = new KeyboardRow();
        yandexRow.add(YANDEX_MONEY_COMMAND);
        var mainRow = new KeyboardRow();
        mainRow.add(MAIN_MENU_ROW);
        listRow.add(yandexRow);
        listRow.add(mainRow);
        var replyKeyboard = new ReplyKeyboardMarkup(listRow);
        replyKeyboard.setResizeKeyboard(true);

        return this.getSendMessage(replyKeyboard, CHOOSE_PAYMENT_METHOD_MESSAGE, receivedMessage.getChatId().toString());
    }

    @Override
    public boolean isCommandSupported(String command) {
        return this.commands.contains(command);
    }

    private SendMessage getSendMessage(ReplyKeyboard replyKeyboard, String message, String chatId) {
        var sendMessage = new SendMessage();
        if (replyKeyboard != null) {
            sendMessage.setReplyMarkup(replyKeyboard);
        }
        sendMessage.setText(message);
        sendMessage.setChatId(chatId);

        return sendMessage;
    }
}
