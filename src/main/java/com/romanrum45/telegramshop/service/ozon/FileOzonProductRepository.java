package com.romanrum45.telegramshop.service.ozon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class FileOzonProductRepository implements OzonProductRepository {

    private final Path availableFilesPath;
    private final Path oldFilesPath;

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

    public Optional<File> getOzonProductFile(OzonType ozonType) {
        try {
            return Files.list(this.availableFilesPath).filter(path -> {
                if (ozonType.equals(OzonType.ACCOUNT_300)) {
                    return path.getFileName().toString().startsWith("300");
                } else if (ozonType.equals(OzonType.ACCOUNT_600)) {
                    return path.getFileName().toString().startsWith("600");
                } else if (ozonType.equals(OzonType.ACCOUNT_900)) {
                    return path.getFileName().toString().startsWith("900");
                }
                return false;
            }).map(Path::toFile).findFirst();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Ошибка в извлечении и передачи файла, тип озон файла: {}", ozonType.name());
            return Optional.empty();
        }
    }

    @Override
    public boolean saveOzonProductFile(String fileName, OzonType ozonType) {
//        if (Files.ex)
//        var file = new File(ava
//        return false;
        return false;
    }
}
