package com.astarel.school.service.impl;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.astarel.school.ApplicationProfile;
import com.astarel.school.service.StorageService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Profile({ApplicationProfile.LOCAL})
public class FileSystemService implements StorageService {

    private Path baseDir;

    @Value("${school.uploaded-files.base-dir}")
    public void setBaseDir(String baseDir) {
        this.baseDir = Path.of(baseDir).toAbsolutePath().normalize();
    }

    private Path resolvePath(String relativePath) {
        String cleanedPath = relativePath.startsWith("/")
                ? relativePath.substring(1)
                : relativePath;

        return baseDir.resolve(cleanedPath).normalize();
    }

    @Override
    public byte[] getFile(String filePath) throws IOException {
        Path path = resolvePath(filePath);
        log.info("Getting file from file system with path - {}", path);

        return Files.readAllBytes(path);
    }

    @Override
    public String putFile(String relativePath, InputStream inputStream) {
        Path path = resolvePath(relativePath);
        log.info("Uploading file to file system with path - {}", path);

        try {
            Files.createDirectories(path.getParent());

            try (InputStream in = inputStream) {
                Files.copy(in, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }

            return path.toString();
        } catch (IOException e) {
            log.error("Unable to upload file to path - {}", path, e);
            throw new RuntimeException("Unable to upload file", e);
        }
    }

    @Override
    public void copyFile(String sourceFilePath, String destinationFilePath) {
        Path sourcePath = resolvePath(sourceFilePath);
        Path destinationPath = resolvePath(destinationFilePath);

        log.debug("Copying file from {} to {}", sourcePath, destinationPath);

        try {
            Files.createDirectories(destinationPath.getParent());
            Files.copy(
                    sourcePath,
                    destinationPath,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            log.error("Unable to copy file from {} to {}", sourcePath, destinationPath, e);
            throw new RuntimeException("Unable to copy file", e);
        }
    }

    @Override
    public void removeFile(String filePath) {
        if (!isNotBlank(filePath)) {
            return;
        }

        Path path = resolvePath(filePath);
        log.info("Removing file from file system with path - {}", path);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("Failed to delete file - {}", path, e);
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    @Override
    public boolean isFileExist(String filePath) {
        Path path = resolvePath(filePath);
        return Files.exists(path);
    }

    @Override
    public InputStream getFilePath(String relativePath) {
        Path path = resolvePath(relativePath);

        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            log.error("Unable to open file input stream - {}", path, e);
            throw new RuntimeException("Unable to open file", e);
        }
    }
}