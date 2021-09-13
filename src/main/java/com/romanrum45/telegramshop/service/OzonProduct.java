package com.romanrum45.telegramshop.service;

import lombok.RequiredArgsConstructor;

import java.util.Locale;

@RequiredArgsConstructor
public class OzonProduct {

    private final String name;

    private final String type;

    private final int count;

    private final int price;

    public String getName() {
        return this.name;
    }

    public String getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }

    public String generateNameButton() {
        return String.format(Locale.ENGLISH, "%s = %s баллов | Цена: %s руб | В наличии: %d шт\n",
                this.name, this.type, price, count);
    }

    public String generateNameButtonWithId(int id) {
        return String.format(Locale.ENGLISH, "%s | ID товара: %d", this.generateNameButton(), id);
    }
}
