package com.romanrum45.telegramshop.service.ozon;

import com.romanrum45.telegramshop.service.Product;
import com.romanrum45.telegramshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class OzonService implements ProductService {

    private final OzonProductRepository ozonProductRepository;

    private final String name;
    private final int price300;
    private final int price600;
    private final int price900;

    @Override
    public List<Product> getProductsInStock() {
        var products = new ArrayList<Product>();
        var ozon300Products = this.ozonProductRepository.getProducts().stream()
                .filter(ozonProduct -> ozonProduct.getOzonType() == OzonType.ACCOUNT_300)
                .collect(Collectors.toList());
        if (!ozon300Products.isEmpty()) {
            var product300 = new Product(this.generateNameButton("300", ozon300Products.size(), this.price300));
            products.add(product300);
        }
        var ozon600Products = this.ozonProductRepository.getProducts().stream()
                .filter(ozonProduct -> ozonProduct.getOzonType() == OzonType.ACCOUNT_600)
                .collect(Collectors.toList());
        if (!ozon600Products.isEmpty()) {
            var product600 = new Product(this.generateNameButton("600", ozon300Products.size(), this.price600));
            products.add(product600);
        }
        var ozon900Products = this.ozonProductRepository.getProducts().stream()
                .filter(ozonProduct -> ozonProduct.getOzonType() == OzonType.ACCOUNT_900)
                .collect(Collectors.toList());
        if (!ozon900Products.isEmpty()) {
            var product900 = new Product(this.generateNameButton("900", ozon300Products.size(), this.price900));
            products.add(product900);
        }
        return products;
    }

    @Override
    public String getNameProduct() {
        return this.name;
    }

    @Override
    public String generateNameButton(String v1, int count, int price) {
        return String.format(Locale.ENGLISH, "%s = %s баллов | Цена: %s руб | В наличии: %d шт",
                getNameProduct(), v1, price, count);
    }


}
