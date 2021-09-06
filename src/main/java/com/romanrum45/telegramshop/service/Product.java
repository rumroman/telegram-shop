package com.romanrum45.telegramshop.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Product {

    private final String name;

    public String getName() {
        return this.name;
    }


}
