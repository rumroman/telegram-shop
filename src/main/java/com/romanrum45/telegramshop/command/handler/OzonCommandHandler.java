package com.romanrum45.telegramshop.command.handler;

import com.romanrum45.telegramshop.customer.AccountService;
import com.romanrum45.telegramshop.service.ozon.OzonService;
import com.romanrum45.telegramshop.service.ozon.OzonType;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OzonCommandHandler implements CommandHandler {

    private final static String MANUAL_MESSAGE = "Короткий видео мануал о том, как войти в аккаунт\n" +
            "https://youtu.be/X7_7qbchGxg\n" +
            "\n" +
            "--------------------------------------------------------------\n" +
            "\n" +
            "Инструкция как пользоваться баллами:\n" +
            "\n" +
            "Компьютер:\n" +
            "\n" +
            "1) Откройте браузер с пустым окном и уже установленным расширением CookieBro\n" +
            "2) Щелкайте на расширение CookieBro, далее выбирайте пункт - Cookie Editor, открывается отдельное окно. Вверху маленькая иконка IMPORT, указывайте выданный .json файл и импортируйте.\n" +
            "3) Все. Теперь набирайте сайт ozon и вы уже авторизованы, можно делать заказ. Баллы на счету - проверьте.\n" +
            "\n" +
            "Если вдруг возникнут проблемы, напишите  @Nikiteng. Покажем расскажем и разберемся. С 7:00 до 21:00 (время московское).\n" +
            "-------------------\n" +
            "Спасибо за покупку и удачного дня!\n";
    private final static String ERROR_MESSAGE = "Произошла непредвиденная ошибка.\nПожалуйста, обратитесть в поддержку - @@Nikiteng";
    private final static String NOT_MONEY_MESSAGE = "Недостаточно средств!";

    private final static String CHOOSE_GOOD_MESSAGE = "Выберите товар";
    private final static String MAIN_MENU_ROW = "Главное меню";

    private final static String ID_COMMAND = "ID товара: ";

    private final String ozonNameCommand;

    private final OzonService ozonService;
    private final AccountService accountService;

    private final String _300Id;
    private final String _600Id;
    private final String _900Id;
    private final List<String> commands;

    public OzonCommandHandler (OzonService ozonService, AccountService accountService, String _300Id, String _600Id, String _900Id) {
        this.ozonService = ozonService;
        this.accountService = accountService;
        this._300Id = _300Id;
        this._600Id = _600Id;
        this._900Id = _900Id;
        this.ozonNameCommand = this.ozonService.getNameProduct();

        this.commands = List.of(ozonNameCommand);
    }

    @Override
    public PartialBotApiMethod<Message> handle(Update update) {
        var receivedMessage = update.getMessage().getText().trim();
        var message = update.getMessage();

        if (receivedMessage.equals(this.ozonNameCommand)) {
            return this.handleOzonNameCommand(message);
        } else if (receivedMessage.contains(ID_COMMAND + this._300Id)) {
            return this.handle300Id(message);
        } else if (receivedMessage.contains(ID_COMMAND + this._600Id)) {
            return this.handle600Id(message);
        } else if (receivedMessage.contains(ID_COMMAND + this._900Id)) {
            return this.handle900Id(message);
        } else {
            throw new UnsupportedOperationException();
        }

    }

    private synchronized PartialBotApiMethod<Message> handle900Id(Message receivedMessage) {
        return null;
    }

    private synchronized PartialBotApiMethod<Message> handle600Id(Message receivedMessage) {
        return null;
    }

    private synchronized PartialBotApiMethod<Message> handle300Id(Message receivedMessage) {
        var chatId = receivedMessage.getChatId().toString();
        var listRow = new ArrayList<KeyboardRow>();
        var mainRow = new KeyboardRow();
        mainRow.add(MAIN_MENU_ROW);
        listRow.add(mainRow);

        var responseMessage = "";

        var ozon300ProductOptional = this.ozonService.getProductsInStock()
                .stream().filter(ozon -> ozon.getType().equals("300"))
                .filter(ozon -> ozon.getCount() > 0)
                .findFirst();

        if (ozon300ProductOptional.isPresent()) {
            var ozon300Product = ozon300ProductOptional.get();
            var balance = this.accountService.getBalance(chatId);
            if (Double.parseDouble(balance) >= ozon300Product.getPrice()) {
                var ozonFileOptional = this.ozonService.getOzonFile(OzonType.ACCOUNT_300);
                if (ozonFileOptional.isPresent()) {
                    var ozonFile = ozonFileOptional.get();
                    var tempName = ozonFile.getName().replace(".json" , "");
                    var id = tempName.substring(4);
                    var caption = "Наименование: OZON | 300 баллов\n" +
                            "Время покупки: " + LocalDateTime.now() +
                            "\nКоличество: 1шт.\n" +
                            "ID: " + id;
                    accountService.updateBalance(chatId, -ozon300Product.getPrice());
                    return this.getSendDocument(ozonFile, caption, chatId);
                } else {
                    log.error("Не удалось найти файл 300 бонусов");
                    responseMessage = ERROR_MESSAGE;
                }
            } else {
                responseMessage = NOT_MONEY_MESSAGE;
            }
        } else {
            responseMessage = ERROR_MESSAGE;
        }
        var replyKeyboard = new ReplyKeyboardMarkup(listRow);
        replyKeyboard.setResizeKeyboard(true);

        return getSendMessage(replyKeyboard, responseMessage, chatId);
    }

    private SendMessage handleOzonNameCommand(Message receivedMessage) {
        var listRow = new ArrayList<KeyboardRow>();
        var mainRow = new KeyboardRow();
        mainRow.add(MAIN_MENU_ROW);
        listRow.add(mainRow);

        var ozonProducts = this.ozonService.getProductsInStock();
        if (!ozonProducts.isEmpty()) {
            ozonProducts.forEach(ozon -> {
                var row = new KeyboardRow();
                if (ozon.getType().equals("300")) {
                    row.add(ozon.generateNameButtonWithId(Integer.parseInt(_300Id)));
                    listRow.add(row);
                } else if (ozon.getType().equals("600")) {
                    row.add(ozon.generateNameButtonWithId(Integer.parseInt(_600Id)));
                    listRow.add(row);
                } else if (ozon.getType().equals("900")) {
                    row.add(ozon.generateNameButtonWithId(Integer.parseInt(_900Id)));
                    listRow.add(row);
                } else {
                    log.error("Type: {} - не поддерживается!", ozon.getType());
                    throw new UnsupportedOperationException("Type: " + ozon.getType() + " не поддерживается!");
                }
            });
        }
        var replyKeyboard = new ReplyKeyboardMarkup(listRow);
        replyKeyboard.setResizeKeyboard(true);

        return this.getSendMessage(replyKeyboard, CHOOSE_GOOD_MESSAGE, receivedMessage.getChatId().toString());
    }

    @Override
    public boolean isCommandSupported(String command) {
        return command.startsWith("OZON = ") || this.commands.contains(command);
    }

    @Override
    public SendMessage getSupportText(String chatId) {
        var listRow = new ArrayList<KeyboardRow>();
        var mainRow = new KeyboardRow();
        mainRow.add(MAIN_MENU_ROW);
        listRow.add(mainRow);
        var replyKeyboard = new ReplyKeyboardMarkup(listRow);
        replyKeyboard.setResizeKeyboard(true);

        return this.getSendMessage(replyKeyboard, MANUAL_MESSAGE, chatId);
    }

    @Override
    public SendMessage notificationSupportUser(String message, String userId) {
        return this.getSendMessage(null, message, userId);
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

    private SendDocument getSendDocument(File ozonFile, String caption, String chatId) {
        var listRow = new ArrayList<KeyboardRow>();
        var mainRow = new KeyboardRow();
        mainRow.add(MAIN_MENU_ROW);
        listRow.add(mainRow);
        var replyKeyboard = new ReplyKeyboardMarkup(listRow);
        replyKeyboard.setResizeKeyboard(true);


        var sendDocument = new SendDocument();
        var inputFile = new InputFile(ozonFile);
        sendDocument.setDocument(inputFile);
        sendDocument.setCaption(caption);
        sendDocument.setReplyMarkup(replyKeyboard);
        sendDocument.setChatId(chatId);

        return sendDocument;
    }


}
