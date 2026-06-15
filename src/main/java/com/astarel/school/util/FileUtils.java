package com.astarel.school.util;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.io.FileUtils.listFiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {
	public static final Optional<File> getDefault(String filePath, String defaultFilePath) {
		File file = checkAndGet(filePath);
		if (null == file) {
			file = checkAndGet(defaultFilePath);
		}

		return ofNullable(file);
	}

	private static final File checkAndGet(String filePath) {
		// TODO check if a better is there for below code with java 8.
		if (log.isDebugEnabled()) {
			log.debug("Checking for file- {}", filePath);
		}

		File resultingFile = null;

		FileChecker fileChecker = new FileChecker(filePath).invoke();
		if (fileChecker.isExists()) {
			resultingFile = fileChecker.getFile();
		} else if (log.isDebugEnabled()) {
			log.debug("File not found- {}", fileChecker.getFile());
		}

		return resultingFile;
	}

	public static File getFileFromByteArray(final byte[] fileBytes) {
		return null;
	}

	@Getter
	@ToString
	private static class FileChecker {

		@Getter(AccessLevel.NONE)
		private String filePath;

		@Getter(AccessLevel.NONE)
		private Path path;

		private File file;
		private boolean exists;

		public FileChecker(String filePath) {
			this.filePath = filePath;
		}

		public FileChecker invoke() {
			path = Paths.get(filePath);
			exists = Files.exists(path);
			file = path.toFile();
			return this;
		}
	}

	public static byte[] getFile(final String filePath) {
		int lastIndexTillDirectoryPath = filePath.lastIndexOf('/');
		return getFile(filePath.substring(lastIndexTillDirectoryPath + 1),
				new File(filePath.substring(0, lastIndexTillDirectoryPath)));
	}

	public static byte[] getFile(final String fileName, final File fileDirectory) {
		byte[] fileBytes = null;

		if (fileDirectory.exists()) {
			final Collection<File> files = listFiles(fileDirectory, null, false);
			for (final File fileLogo : files) {
				if (fileLogo.getName().contains(fileName)) {
					try {
						fileBytes = IOUtils.toByteArray(new FileInputStream(fileLogo));
					} catch (IOException e) {
						log.error("Error converting file- {} to byte array", fileLogo, e);
					}
					break;
				}
			}
		}

		return fileBytes;
	}

	public static long convertByteToMegaBytes(long bytes) {
		long megaBytes = bytes / (1024 * 1024);
		return megaBytes;
	}

	public static String getDirectoryFromFilePath(String fullFilePath) {
		int index = fullFilePath.lastIndexOf("/");
		String path = fullFilePath.substring(0, index + 1);
		return path;
	}

	public static String prepareFileNameWithTime(final MultipartFile file) {
		String filename = file.getOriginalFilename();
		String fileBaseName = FilenameUtils.getBaseName(filename);
		return format("%s_%s.%s", fileBaseName.substring(0, fileBaseName.length() - 1),
				new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()), FilenameUtils.getExtension(filename));
	}

}
