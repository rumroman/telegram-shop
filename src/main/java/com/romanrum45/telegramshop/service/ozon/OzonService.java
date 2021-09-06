package com.romanrum45.telegramshop.service.ozon;

import com.romanrum45.telegramshop.repository.CustomerAccountRepository;
import com.romanrum45.telegramshop.repository.PaymentTransactionRepository;
import com.romanrum45.telegramshop.service.Product;
import com.romanrum45.telegramshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class OzonService implements ProductService {

    private final PaymentTransactionRepository paymentTransactionRepository;

    private final CustomerAccountRepository customerAccountRepository;


    @Override
    public List<Product> getProductsInStock() {


        return null;
    }
}
