package ua.gexlq.TelegramStudyBot.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import ua.gexlq.TelegramStudyBot.exceptions.FileServiceException;
import ua.gexlq.TelegramStudyBot.service.FileStorageService;

@Service
public class TelegramFileStorageService implements FileStorageService {

	@Value("${token}")
	private String token;

	@Value("${service.file_storage.uri}")
	private String fileStorageUri;

	@Override
	public byte[] downloadFile(String downloadFilePath) {
		String fullUri = fileStorageUri.replace("{token}", token).replace("{filePath}", downloadFilePath);
		URL urlObj = null;
		try {
			urlObj = new URL(fullUri);
		} catch (MalformedURLException e) {
			throw new FileServiceException(e);
		}

		try (InputStream is = urlObj.openStream()) {
			return is.readAllBytes();
		} catch (IOException e) {
			throw new FileServiceException(urlObj.toExternalForm(), e);
		}
	}
}
