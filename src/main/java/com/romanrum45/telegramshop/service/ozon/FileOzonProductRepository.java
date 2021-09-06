package com.romanrum45.telegramshop.service.ozon;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FileOzonProductRepository implements OzonProductRepository {

    private final Path availableFilesPath;
    private final Path oldFilesPath;

    private final String prefix;
    private final String postfix;

    private static final String ACCOUNT_300 = "300_";
    private static final String ACCOUNT_600 = "600_";
    private static final String ACCOUNT_900 = "900_";

    @Override
    public List<OzonProductEntity> getProducts() {
        try {
            return Files.list(availableFilesPath)
                    .map(Path::toFile)
                    .filter(file -> {
                        var fileName = file.getName();
                        return (fileName.startsWith(ACCOUNT_300) || fileName.startsWith(ACCOUNT_600) ||
                                fileName.startsWith(ACCOUNT_900)) && fileName.endsWith(".json");
                    }).map(file -> {
                        if (file.getName().startsWith(ACCOUNT_300)) {
                            return OzonProductEntity.builder()
                                    .ozonType(OzonType.ACCOUNT_300)
                                    .path(file.getAbsolutePath())
                                    .build();
                        } else if (file.getName().startsWith(ACCOUNT_600)) {
                            return OzonProductEntity.builder()
                                    .ozonType(OzonType.ACCOUNT_600)
                                    .path(file.getAbsolutePath())
                                    .build();
                        } else if (file.getName().startsWith(ACCOUNT_900)) {
                            return OzonProductEntity.builder()
                                    .ozonType(OzonType.ACCOUNT_900)
                                    .build();
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return List.of();
    }
}
