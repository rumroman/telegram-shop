package com.romanrum45.telegramshop.command.handler;

import com.romanrum45.telegramshop.service.ozon.OzonService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OzonCommandHandler implements CommandHandler {

    private final static String CHOOSE_GOOD_MESSAGE = "Выберите товар";
    private final static String MAIN_MENU_ROW = "Главное меню";

    private final String ozonNameCommand;

    private final OzonService ozonService;
    private final String _300Id;
    private final String _600Id;
    private final String _900Id;
    private final List<String> commands;

    public OzonCommandHandler (OzonService ozonService, String _300Id, String _600Id, String _900Id) {
        this.ozonService = ozonService;
        this._300Id = _300Id;
        this._600Id = _600Id;
        this._900Id = _900Id;
        this.ozonNameCommand = this.ozonService.getNameProduct();

        this.commands = List.of(ozonNameCommand);
    }

    @Override
    public SendMessage handle(Update update) {
        var receivedMessage = update.getMessage().getText().trim();
        var message = update.getMessage();

        if (receivedMessage.equals(this.ozonNameCommand)) {
            return this.handleOzonNameCommand(message);
        } else if (receivedMessage.contains("ID товара: " + this._300Id)) {
            return this.handle300Id(message);
        } else if (receivedMessage.contains("ID товара: " + this._600Id)) {
            return this.handle600Id(message);
        } else if (receivedMessage.contains("ID товара: " + this._900Id)) {
            return this.handle900Id(message);
        } else {
            throw new UnsupportedOperationException();
        }

    }

    private SendMessage handle900Id(Message message) {
        return null;
    }

    private SendMessage handle600Id(Message message) {
        return null;
    }

    private SendMessage handle300Id(Message message) {
        return null;
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
        return this.commands.contains(command);
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
