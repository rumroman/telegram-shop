package com.romanrum45.telegramshop.command.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler {

    /**
     * Обработка команды.
     * @param update update от пользователя.
     */
    SendMessage handle(Update update);

    /**
     * Поддерживается ли команда?
     *
     * @param command сообщение от пользователя.
     * @return true или false.
     */
    boolean isCommandSupported(String command);

}
