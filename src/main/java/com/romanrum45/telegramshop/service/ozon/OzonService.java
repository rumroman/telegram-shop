package com.romanrum45.telegramshop.service.ozon;

import com.romanrum45.telegramshop.customer.CustomerAccountRepository;
import com.romanrum45.telegramshop.customer.PaymentTransactionRepository;
import com.romanrum45.telegramshop.service.Product;
import com.romanrum45.telegramshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class OzonService implements ProductService {

    private final PaymentTransactionRepository paymentTransactionRepository;

    private final CustomerAccountRepository customerAccountRepository;

    private final OzonProductRepository ozonProductRepository;

    private final String price;

    @Override
    public List<Product> getProductsInStock() {
        var products = new ArrayList<Product>();
        var ozon300Products = this.ozonProductRepository.getProducts().stream()
                .filter(ozonProduct -> ozonProduct.getOzonType() == OzonType.ACCOUNT_300)
                .collect(Collectors.toList());
        if (!ozon300Products.isEmpty()) {
            var product300 = new Product();

        }
        var ozon600Products = this.ozonProductRepository.getProducts().stream()
                .filter(ozonProduct -> ozonProduct.getOzonType() == OzonType.ACCOUNT_600)
                .collect(Collectors.toList());
        var ozon900Products = this.ozonProductRepository.getProducts().stream()
                .filter(ozonProduct -> ozonProduct.getOzonType() == OzonType.ACCOUNT_900)
                .collect(Collectors.toList());
        return List.of();
    }

    @Override
    public String getNameProduct() {
        return "OZON";
    }


}
