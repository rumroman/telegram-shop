package com.romanrum45.telegramshop.repository;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "paymentTransaction")
public class PaymentTransactionEntity {

    /**
     * Идентификатор операции в истории счета получателя.
     */
    @Id
    private String operationId;

    /**
     * Для переводов из кошелька — p2p-incoming
     * Для переводов с произвольной карты — card-incoming.
     */
    private String notificationType;

    /**
     * Сумма операции.
     */
    private String amount;

    /**
     * Сумма, которая списана со счета отправителя.
     */
    private String withdrawAmount;

    /**
     * Код валюты счета пользователя. Всегда 643 (рубль РФ согласно ISO 4217).
     */
    private String currency;

    /**
     * Дата и время совершения перевода.
     */
    private LocalDateTime dateTime;

    /**
     * Для переводов из кошелька — номер счета отправителя.
     * Для переводов с произвольной карты — параметр содержит пустую строку.
     */
    private String sender;

    /**
     * Флаг означает, что пользователь не получил перевод.
     *
     * Возможные причины: Перевод заморожен, так как на счете получателя достигнут лимит доступного остатка.
     * Отображается в поле hold ответа метода account-info. Перевод защищен кодом протекции.
     * В этом случае codepro=true.
     */
    private boolean unaccepted;

}
