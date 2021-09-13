package com.romanrum45.telegramshop.service.ozon;

import com.romanrum45.telegramshop.service.OzonProduct;
import com.romanrum45.telegramshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public List<OzonProduct> getProductsInStock() {
        var products = new ArrayList<OzonProduct>();
        var ozon300Products = this.ozonProductRepository.getProducts().stream()
                .filter(ozonProduct -> ozonProduct.getOzonType() == OzonType.ACCOUNT_300)
                .collect(Collectors.toList());
        if (!ozon300Products.isEmpty()) {
            var product300 = new OzonProduct(this.name , "300", ozon300Products.size(), this.price300);
            products.add(product300);
        }
        var ozon600Products = this.ozonProductRepository.getProducts().stream()
                .filter(ozonProduct -> ozonProduct.getOzonType() == OzonType.ACCOUNT_600)
                .collect(Collectors.toList());
        if (!ozon600Products.isEmpty()) {
            var product600 = new OzonProduct(this.name, "600", ozon600Products.size(), this.price600);
            products.add(product600);
        }
        var ozon900Products = this.ozonProductRepository.getProducts().stream()
                .filter(ozonProduct -> ozonProduct.getOzonType() == OzonType.ACCOUNT_900)
                .collect(Collectors.toList());
        if (!ozon900Products.isEmpty()) {
            var product900 = new OzonProduct(this.name, "900", ozon900Products.size(), this.price900);
            products.add(product900);
        }
        return products;
    }

    public Optional<File> getOzonFile(OzonType ozonType) {
        return this.ozonProductRepository.getOzonProductFile(ozonType);
    }

    public boolean uploadOzonFile(OzonType ozonType) {
//        return this.ozonProductRepository.saveOzonProductFile(ozonType);
        return false;
    }

    @Override
    public String getNameProduct() {
        return this.name;
    }

//    @Override
//    public String generateNameButton(String v1, int count, int price) {
//        return String.format(Locale.ENGLISH, "%s = %s баллов | Цена: %s руб | В наличии: %d шт\n",
//                getNameProduct(), v1, price, count);
//    }
//
//    @Override
//    public String generateNameButtonWithId(String v1, int count, int price, int id) {
//        return String.format(Locale.ENGLISH, "%s | ID товара: %d", this.generateNameButton(v1, count, price), id);
//    }


}
