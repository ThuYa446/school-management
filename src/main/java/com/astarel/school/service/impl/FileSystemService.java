package com.astarel.school.service.impl;


import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.astarel.school.ApplicationProfile;
import com.astarel.school.service.StorageService;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@Profile({ApplicationProfile.LOCAL})
public class FileSystemService implements StorageService {

  private String BASE_DIR;


  @Value("${mathilda.uploaded-files.base-dir}")
  public void setBaseDir(String baseDir) {
    BASE_DIR = baseDir;
  }


  public byte[] getFile(final String filePath) throws IOException {
    log.info("Getting file from file system with path- {}", filePath);
    System.out.println("file path : " + "C:" + BASE_DIR + filePath);
    return com.astarel.school.util.FileUtils.getFile("C:" + BASE_DIR + filePath);
  }

  public String putFile(String relativePath, final InputStream inputStream) {
    relativePath = "C:" + BASE_DIR + relativePath;
    log.info("Uploading to File system with path - {}", relativePath);
    try {
      FileUtils.copyInputStreamToFile(inputStream, new File(relativePath));
    }
    catch (IOException e) {
      log.error("Unable to uploading to File system with path - {}", relativePath);
    }
    return relativePath;
  }

  public void copyFile(String sourceFilePath, String destinationFilePath) {
    sourceFilePath = "C:" + BASE_DIR + sourceFilePath;
    destinationFilePath = "C:" + BASE_DIR + destinationFilePath;
    log.debug("Copying file to File system from path - {} to path - {}", sourceFilePath, destinationFilePath);
    try {
      File companyResumeDirectory = new File(destinationFilePath.substring(0, destinationFilePath.lastIndexOf('/')));
      if (!companyResumeDirectory.exists()) {
        companyResumeDirectory.mkdir();
      }
      FileCopyUtils.copy(new File(sourceFilePath), new File(destinationFilePath));
    }
    catch (IOException e) {
      log.error("Unable to copy file to File system from path - {} to path - {}", sourceFilePath, destinationFilePath);
    }
  }

  public void removeFile(final String filePath) {
	if (isNotBlank(filePath))
		System.out.println("C:" + BASE_DIR + filePath);
		try {
		    Files.delete(Paths.get("C:" + BASE_DIR + filePath));
		} catch (IOException e) {
		    System.out.println("Failed to delete the file: " + e.getMessage());
		}
  } 

  @Override
  public boolean isFileExist(final String filePath) {
    return new File("C:" + BASE_DIR + filePath).exists();
  }


	@Override
	public InputStream getFilePath(final String relativePath) {
	    String fullPath = "C:" + BASE_DIR + relativePath;
	    File file = new File(fullPath);
	    
	    try {
			return new FileInputStream(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	}
  
}

