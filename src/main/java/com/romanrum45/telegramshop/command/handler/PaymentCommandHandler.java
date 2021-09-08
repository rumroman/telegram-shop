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
        pay50Btn.setText(PAY_50_BUTTON);
        var sum50 = String.format("%4f", 50 / 0.98);
        var url50 = String.format(Locale.ENGLISH, "https://money.yandex.ru/quickpay/confirm.xml?receiver=%s&formcomment=Пополнение+баланса+в+боте&label=%s&quickpay-form=donate&targets=Пополнение+баланса+для+аккаунта+%s&sum=%s",
                this.yandexAccount, chatId, chatId, sum50);
        pay50Btn.setUrl(url50);

        var pay100Btn = new InlineKeyboardButton();
        pay100Btn.setText(PAY_100_BUTTON);
        var sum100 = String.format("%4f", 100 / 0.98);
        var url100 = String.format(Locale.ENGLISH, "https://money.yandex.ru/quickpay/confirm.xml?receiver=%s&formcomment=Пополнение+баланса+в+боте&label=%s&quickpay-form=donate&targets=Пополнение+баланса+для+аккаунта+%s&sum=%s",
                this.yandexAccount, chatId, chatId, sum100);
        pay100Btn.setUrl(url100);

        var pay250Btn = new InlineKeyboardButton();
        pay250Btn.setText(PAY_250_BUTTON);
        var sum250 = String.format("%4f", 250 / 0.98);
        var url250 = String.format(Locale.ENGLISH, "https://money.yandex.ru/quickpay/confirm.xml?receiver=%s&formcomment=Пополнение+баланса+в+боте&label=%s&quickpay-form=donate&targets=Пополнение+баланса+для+аккаунта+%s&sum=%s",
                this.yandexAccount, chatId, chatId, sum250);
        pay250Btn.setUrl(url250);

        var pay500Btn = new InlineKeyboardButton();
        pay500Btn.setText(PAY_500_BUTTON);
        var sum500 = String.format("%4f", 500 / 0.98);
        var url500 = String.format(Locale.ENGLISH, "https://money.yandex.ru/quickpay/confirm.xml?receiver=%s&formcomment=Пополнение+баланса+в+боте&label=%s&quickpay-form=donate&targets=Пополнение+баланса+для+аккаунта+%s&sum=%s",
                this.yandexAccount, chatId, chatId, sum500);
        pay500Btn.setUrl(url500);

        var pay1000Btn = new InlineKeyboardButton();
        pay1000Btn.setText(PAY_1000_BUTTON);
        var sum1000 = String.format("%4f", 1000 / 0.98);
        var url1000 = String.format(Locale.ENGLISH, "https://money.yandex.ru/quickpay/confirm.xml?receiver=%s&formcomment=Пополнение+баланса+в+боте&label=%s&quickpay-form=donate&targets=Пополнение+баланса+для+аккаунта+%s&sum=%s",
                this.yandexAccount, chatId, chatId, sum1000);
        pay1000Btn.setUrl(url1000);

        var list1 = List.of(pay30Btn, pay50Btn);
        var list2 = List.of(pay100Btn, pay250Btn);
        var list3 = List.of(pay500Btn, pay1000Btn);

        inlineKeyboard.setKeyboard(List.of(list1, list2, list3));

        return this.getSendMessage(inlineKeyboard, ENTER_AMOUNT_MESSAGE, chatId);
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
