package com.romanrum45.telegramshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class TelegramShopService {


    private final List<ProductService> productServices;


    public List<String> AllProducts() {
        return productServices.stream()
                .filter(productService -> !productService.getProductsInStock().isEmpty())
                .flatMap(productService -> productService.getProductsInStock().stream())
                .map(Product::getName)
                .collect(Collectors.toList());
    }


}
