package com.romanrum45.telegramshop.service;

import java.util.List;

public interface ProductService {



    List<OzonProduct> getProductsInStock();

    String getNameProduct();
//
//    String generateNameButton(String v1, int count, int price);


}
