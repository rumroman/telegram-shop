package com.romanrum45.telegramshop.service.ozon;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface OzonProductRepository {

    List<OzonProductEntity> getProducts();

    Optional<File> getOzonProductFile(OzonType ozonType);

    boolean saveOzonProductFile(String fileName, OzonType ozonType);


}
