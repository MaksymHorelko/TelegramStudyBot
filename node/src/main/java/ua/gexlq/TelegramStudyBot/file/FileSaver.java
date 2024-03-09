package ua.gexlq.TelegramStudyBot.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ua.gexlq.TelegramStudyBot.exceptions.FileServiceException;
import ua.gexlq.TelegramStudyBot.service.enums.SupportedMimeType;

@Slf4j
@Component
public class FileSaver {

	@Value("${file.path}")
	private String folderPath;

	public String saveFileLocally(byte[] fileData, String mimeType) {
		String extension = SupportedMimeType.mimeTypeToExtension(mimeType);
		String fullFilePath = folderPath + generateUniqueFileName() + "." + extension;

		try {
			Files.write(Paths.get(fullFilePath), fileData);
			log.info("File is saved: " + fullFilePath);
			return fullFilePath;
		} catch (IOException e) {
			throw new FileServiceException("Error saving file locally: " + e.getMessage());
		}
	}

	public String generateUniqueFileName() {
		return "file_" + System.currentTimeMillis();
	}
}
