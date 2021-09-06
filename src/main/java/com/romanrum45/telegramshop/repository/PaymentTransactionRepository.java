package com.romanrum45.telegramshop.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentTransactionRepository extends MongoRepository<PaymentTransactionEntity, String> {
}
