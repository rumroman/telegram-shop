package com.romanrum45.telegramshop.service.ozon;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
public class OzonProductEntity {

    private String path;

    private OzonType ozonType;

}
