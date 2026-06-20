package com.astarel.school.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

class FileUtilsTest {

	@TempDir
	Path tempDir;

	@Test
	void getDefaultReturnsPrimaryFileWhenItExists() throws Exception {
		Path primary = Files.writeString(tempDir.resolve("primary.txt"), "primary", StandardCharsets.UTF_8);
		Path fallback = Files.writeString(tempDir.resolve("fallback.txt"), "fallback", StandardCharsets.UTF_8);

		Optional<java.io.File> result = FileUtils.getDefault(primary.toString(), fallback.toString());

		assertThat(result).isPresent();
		assertThat(result.get()).hasName("primary.txt");
	}

	@Test
	void getDefaultFallsBackWhenPrimaryIsMissing() throws Exception {
		Path fallback = Files.writeString(tempDir.resolve("fallback.txt"), "fallback", StandardCharsets.UTF_8);

		Optional<java.io.File> result = FileUtils.getDefault(tempDir.resolve("missing.txt").toString(), fallback.toString());

		assertThat(result).isPresent();
		assertThat(result.get()).hasName("fallback.txt");
	}

	@Test
	void getFileReadsBytesFromTheContainingDirectory() throws Exception {
		Path file = Files.writeString(tempDir.resolve("report.pdf"), "school-report", StandardCharsets.UTF_8);
		String normalizedPath = file.toString().replace('\\', '/');

		byte[] result = FileUtils.getFile(normalizedPath);

		assertThat(result).isEqualTo("school-report".getBytes(StandardCharsets.UTF_8));
	}

	@Test
	void helperMethodsReturnExpectedMetadata() {
		MockMultipartFile file = new MockMultipartFile("file", "sample.pdf", "application/pdf",
				"content".getBytes(StandardCharsets.UTF_8));

		assertThat(FileUtils.convertByteToMegaBytes(5L * 1024 * 1024)).isEqualTo(5);
		assertThat(FileUtils.getDirectoryFromFilePath("/school/files/report.pdf")).isEqualTo("/school/files/");
		assertThat(FileUtils.prepareFileNameWithTime(file)).startsWith("sampl_").endsWith(".pdf");
	}
}
