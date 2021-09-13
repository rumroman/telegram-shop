package com.romanrum45.telegramshop.command.handler;

import com.romanrum45.telegramshop.customer.AccountService;
import com.romanrum45.telegramshop.service.ozon.OzonService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
public class MainCommandHandler implements CommandHandler {

    private final static String BUY_COMMAND = "Купить";
    private final static String GOODS_COMMAND = "Наличие товаров";
    private final static String ACCOUNT_COMMAND = "Личный кабинет";
    private final static String BALANCE_COMMAND = "Баланс";
    private final static String HELP_COMMAND = "Помощь";
    private final static String RULES_COMMAND = "Правила";
    private final static String MAIN_COMMAND = "Главное меню";
    private final static String PURCHASES_COMMAND = "Покупки";
    private final static String DEPOSITS_COMMAND = "Пополнения";
    private final static String BACK_COMMAND = "Назад";

    private final static String RULES_MESSAGE = "Делая покупку в данном боте, вы соглашаетесь с тем, что ознакомлены и согласны с данными правилами:\n" +
            "1. Общее\n" +
            "1.1. Желательно записывать видео покупки в случае невалида/ошибки!\n" +
            "1.2. Валидный товар возврату или обмену не подлежит.\n" +
            "2. OZON\n" +
            "2.1. После успешного списания баллов, претензии не принимаются.";
    private final static String USE_KEYBOARD_MESSAGE = "Используйте клавиатуру";
    private final static String HELP_MESSAGE = "Поддержка - @";
    private final static String BALANCE_MESSAGE = "Ваш баланс: ";
    private final static String SELECT_CATEGORY_MESSAGE = "Выберите категорию";
    private final static String UP_BALANCE_MESSAGE = "Пополнить баланс";

    private final static String COMMAND_UNSUPPORTED = "Команда не реализована!";


    private final OzonService ozonService;
    private final AccountService accountService;

    private final List<String> commands;

    private final String supportUserName;

    public MainCommandHandler(OzonService ozonService,
                              AccountService accountService, String supportUserName) {
        this.ozonService = ozonService;
        this.accountService = accountService;
        this.supportUserName = supportUserName;

        this.commands = List.of(BUY_COMMAND, GOODS_COMMAND, ACCOUNT_COMMAND, BALANCE_COMMAND, HELP_COMMAND, RULES_COMMAND,
                MAIN_COMMAND, PURCHASES_COMMAND, DEPOSITS_COMMAND, BACK_COMMAND);
    }


    @Override
    public SendMessage handle(Update update) {
        var receivedMessage = update.getMessage().getText().trim();
        var message = update.getMessage();
        switch (receivedMessage) {
            case BUY_COMMAND:
                return this.handleBuyCommand(message);
            case GOODS_COMMAND:
                return this.handleGoodsCommand(message);
            case ACCOUNT_COMMAND:
                return this.handleAccountCommand(message);
            case BALANCE_COMMAND:
                return this.handleBalanceCommand(message);
            case HELP_COMMAND:
                return this.handleHelpCommand(message);
            case RULES_COMMAND:
                return this.handleRulesCommand(message);
            case MAIN_COMMAND:
                return this.handleMainCommand(message);
            case PURCHASES_COMMAND:
                return this.handlePurchasesCommand(message);
            case DEPOSITS_COMMAND:
                return this.handleDepositsCommand(message);
            case BACK_COMMAND:
                return this.handleMainCommand(message);
            default:
                return this.getSendMessage(this.getMainMenuKeyboard(), USE_KEYBOARD_MESSAGE, message.getChatId().toString());
        }
    }

    @Override
    public boolean isCommandSupported(String command) {
        return this.commands.contains(command);
    }

    private SendMessage handleDepositsCommand(Message receivedMessage) {
        return this.unsupportedCommand(receivedMessage);
    }

    private SendMessage handlePurchasesCommand(Message receivedMessage) {
        return this.unsupportedCommand(receivedMessage);
    }

    private SendMessage handleMainCommand(Message receivedMessage) {
        return this.getSendMessage(this.getMainMenuKeyboard(), MAIN_COMMAND, receivedMessage.getChatId().toString());
    }

    private SendMessage handleRulesCommand(Message receivedMessage) {
        return this.getSendMessage(this.getMainMenuKeyboard(), RULES_MESSAGE, receivedMessage.getChatId().toString());
    }

    private SendMessage handleHelpCommand(Message receivedMessage) {
        return this.getSendMessage(this.getMainMenuKeyboard(), HELP_MESSAGE + this.supportUserName,
                receivedMessage.getChatId().toString());
    }

    private SendMessage handleBalanceCommand(Message receivedMessage) {
        var chatId = receivedMessage.getChatId().toString();
        var balance = this.accountService.getBalance(chatId);
        var balanceMessage = BALANCE_MESSAGE + balance + " руб.";
        var listRow = new ArrayList<KeyboardRow>();
        var upBalance = new KeyboardRow();
        upBalance.add(UP_BALANCE_MESSAGE);
        var main = new KeyboardRow();
        main.add(MAIN_COMMAND);
        listRow.add(upBalance);
        listRow.add(main);

        var replyKeyboard = new ReplyKeyboardMarkup(listRow);
        replyKeyboard.setResizeKeyboard(true);

        return this.getSendMessage(replyKeyboard, balanceMessage, chatId);
    }

    private SendMessage handleAccountCommand(Message message) {
        var chatId = message.getChatId().toString();

        var accountMessage = String.format(Locale.ENGLISH, "Ваш профиль:\n" +
                        "   Ваш ID: %s\n" +
                        "   Ваш никнейм: %s\n" +
                        "   Ваш текущий баланс: %s руб.",
//                        "   Покупок на сумму: %d руб.",
                chatId, this.accountService.getNickname(chatId), this.accountService.getBalance(chatId));
//                this.accountService.getDepositsSum(chatId));

        var listRow = new ArrayList<KeyboardRow>();
//        var row = new KeyboardRow();
//        row.add(DEPOSITS_COMMAND);
//        row.add(PURCHASES_COMMAND);
//        listRow.add(row);
        var mainRow = new KeyboardRow();
        mainRow.add(MAIN_COMMAND);
        listRow.add(mainRow);
        var replyKeyboard = new ReplyKeyboardMarkup(listRow);
        replyKeyboard.setResizeKeyboard(true);

        return this.getSendMessage(replyKeyboard, accountMessage, chatId);
    }

    private SendMessage handleBuyCommand(Message receivedMessage) {
        var listRow = new ArrayList<KeyboardRow>();
        var backRow = new KeyboardRow();
        backRow.add(BACK_COMMAND);
        var row = new KeyboardRow();
        row.add(this.ozonService.getNameProduct());
        listRow.add(row);
        listRow.add(backRow);
        var replyKeyboard = new ReplyKeyboardMarkup(listRow);
        replyKeyboard.setResizeKeyboard(true);

        return this.getSendMessage(replyKeyboard, SELECT_CATEGORY_MESSAGE, receivedMessage.getChatId().toString());
    }

    private SendMessage handleGoodsCommand(Message receivedMessage) {
        return this.unsupportedCommand(receivedMessage);
    }

    private ReplyKeyboardMarkup getMainMenuKeyboard() {
        var listRow = new ArrayList<KeyboardRow>();
        var row = new KeyboardRow();
        row.add(BUY_COMMAND);
        row.add(GOODS_COMMAND);
        listRow.add(row);
        row = new KeyboardRow();
        row.add(ACCOUNT_COMMAND);
        row.add(BALANCE_COMMAND);
        listRow.add(row);
        row = new KeyboardRow();
        row.add(HELP_COMMAND);
        row.add(RULES_COMMAND);
        listRow.add(row);
        var replyKeyboard = new ReplyKeyboardMarkup(listRow);
        replyKeyboard.setResizeKeyboard(true);
        return replyKeyboard;
    }

    private SendMessage unsupportedCommand(Message receivedMessage) {
        return this.getSendMessage(null, COMMAND_UNSUPPORTED, receivedMessage.getChatId().toString());
    }

    private SendMessage getSendMessage(ReplyKeyboardMarkup replyKeyboard, String message, String chatId) {
        var sendMessage = new SendMessage();
        if (replyKeyboard != null) {
            sendMessage.setReplyMarkup(replyKeyboard);
        }
        sendMessage.setText(message);
        sendMessage.setChatId(chatId);

        return sendMessage;
    }
}
