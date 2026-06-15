package com.astarel.school.service;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {
	
      InputStream getFilePath(final String relativePath);

	  byte[] getFile(final String relativePath) throws IOException;

	  String putFile(final String relativePath, final InputStream inputStream);

	  void copyFile(final String sourceFilePath, final String destinationFilePath);

	  void removeFile(final String filePath);

	  boolean isFileExist(final String filePath);
}
