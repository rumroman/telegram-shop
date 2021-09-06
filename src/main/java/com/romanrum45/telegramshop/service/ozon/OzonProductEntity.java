package com.romanrum45.telegramshop.service.ozon;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document(collection = "ozonProduct")
public class OzonProductEntity {

    @Id
    private String id;

}
