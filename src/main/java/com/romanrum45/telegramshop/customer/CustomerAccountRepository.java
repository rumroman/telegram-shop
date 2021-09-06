package com.romanrum45.telegramshop.customer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerAccountRepository extends MongoRepository<CustomerAccountEntity, String> {


}
