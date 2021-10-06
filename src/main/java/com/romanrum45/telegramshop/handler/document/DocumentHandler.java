package com.romanrum45.telegramshop.handler.document;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Обработчик присылаемых файлов.
 */
public interface DocumentHandler {

    /**
     * Обработка команды.
     * @param update update от пользователя.
     */
    SendMessage handle(Update update);

    /**
     * Поддерживается ли команда?
     *
     * @param caption подпись файла от пользователя.
     * @return true или false.
     */
    boolean isSupported(String caption);

}
