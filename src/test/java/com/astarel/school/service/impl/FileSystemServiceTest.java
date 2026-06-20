package com.astarel.school.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileSystemServiceTest {

	@TempDir
	Path tempDir;

	private FileSystemService service;

	@BeforeEach
	void setUp() {
		service = new FileSystemService();
		service.setBaseDir(tempDir.toString());
	}

	@Test
	void putGetAndRemoveFileRoundTrip() throws Exception {
		String storedPath = service.putFile("/docs/file.txt",
				new ByteArrayInputStream("school".getBytes(StandardCharsets.UTF_8)));

		assertThat(storedPath.replace("\\", "/"))
        .endsWith("/docs/file.txt");
		assertThat(service.isFileExist("/docs/file.txt")).isTrue();
		assertThat(service.getFile("/docs/file.txt")).isEqualTo("school".getBytes(StandardCharsets.UTF_8));
		try (InputStream inputStream = service.getFilePath("/docs/file.txt")) {
		    assertThat(inputStream).isNotNull();
		}

		service.removeFile("/docs/file.txt");

		assertThat(service.isFileExist("/docs/file.txt")).isFalse();
	}

	@Test
	void copyFileDuplicatesTheSourceIntoDestinationFolder() throws Exception {
		service.putFile("/source.txt", new ByteArrayInputStream("copy-me".getBytes(StandardCharsets.UTF_8)));

		service.copyFile("/source.txt", "/archive/copied.txt");

		Path copiedFile = tempDir.resolve("archive").resolve("copied.txt");
		assertThat(Files.exists(copiedFile)).isTrue();
		assertThat(Files.readString(copiedFile, StandardCharsets.UTF_8)).isEqualTo("copy-me");
	}
}
