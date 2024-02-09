package ua.gexlq.TelegramStudyBot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.dao.DocumentMetadataDAO;
import ua.gexlq.TelegramStudyBot.entity.AppUser;
import ua.gexlq.TelegramStudyBot.entity.DocumentMetadata;
import ua.gexlq.TelegramStudyBot.entity.DownloadedFile;
import ua.gexlq.TelegramStudyBot.exceptions.UploadFileException;
import ua.gexlq.TelegramStudyBot.service.FileService;
import ua.gexlq.TelegramStudyBot.service.enums.SupportedMimeType;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

	@Value("${token}")
	private String token;

	@Value("${service.file_info.uri}")
	private String fileInfoUri;

	@Value("${service.file_storage.uri}")
	private String fileStorageUri;

	@Value("${file.path}")
	private String folderPath;

	private final AppUserDAO appUserDAO;
	private final DocumentMetadataDAO documentInfoDAO;

	@Override
	public DownloadedFile downloadDocument(Update update) {
		var telegramMessage = update.getMessage();

		String fileId = telegramMessage.getDocument().getFileId();
		ResponseEntity<String> response = getFilePath(fileId);

		if (response.getStatusCode() == HttpStatus.OK) {

			JSONObject jsonObject = new JSONObject(response.getBody());
			String filePath = String.valueOf(jsonObject.getJSONObject("result").getString("file_path"));
			byte[] fileInByte = downloadFile(filePath);

			String fullFilePath = saveFileLocally(fileInByte, update.getMessage().getDocument().getMimeType());

			return buildDownloadeddFile(update, fullFilePath);
		}

		else {
			throw new UploadFileException("Bad response from telegram service: " + response);
		}
	}

	private DownloadedFile buildDownloadeddFile(Update update, String filePath) {

		var document = update.getMessage().getDocument();

		Long authorId = update.getMessage().getFrom().getId();

		String authorUserName = update.getMessage().getFrom().getUserName();

		String docName = document.getFileName();
		
		String telegramFileId = document.getFileId();

		String mimeType = document.getMimeType();

		Long fileSize = Long.valueOf(document.getFileSize());

		AppUser user = appUserDAO.findUserByTelegramUserId(authorId);
		var documentRate = user.getUpcomingDocumentRating();

		String workCode = documentRate.getWorkCode();
		String rateContent = documentRate.getRateContent();
		String rateImplementaion = documentRate.getRateImplementaion();
		String rateMark = documentRate.getRateMark();

		var documentMetadata = DocumentMetadata.builder().authorId(authorId).authorUserName(authorUserName)
				.workCode(workCode).rateContent(rateContent).rateImplementaion(rateImplementaion).rateMark(rateMark)
				.docName(docName).telegramFileId(telegramFileId).mimeType(mimeType).fileSize(fileSize).build();
		documentInfoDAO.save(documentMetadata);

		return DownloadedFile.builder().documentMetadata(documentMetadata).filePath(filePath).build();
	}

	private ResponseEntity<String> getFilePath(String fileId) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> request = new HttpEntity<>(headers);

		return restTemplate.exchange(fileInfoUri, HttpMethod.GET, request, String.class, token, fileId);
	}

	private String saveFileLocally(byte[] fileData, String mimeType) {

		String extension = SupportedMimeType.mimeTypeToExtension(mimeType);

		String fullFilePath = folderPath + generateUniqueFileName() + "." + extension;

		try {
			Files.write(Paths.get(fullFilePath), fileData);
			log.info("File is saved: " + fullFilePath);
			return fullFilePath;
		} catch (IOException e) {
			throw new UploadFileException("Error saving file locally: " + e.getMessage());
		}
	}

	private String generateUniqueFileName() {
		return "file_" + System.currentTimeMillis();
	}

	private byte[] downloadFile(String downloadFilePath) {
		String fullUri = fileStorageUri.replace("{token}", token).replace("{filePath}", downloadFilePath);
		URL urlObj = null;
		try {
			urlObj = new URL(fullUri);
		} catch (MalformedURLException e) {
			throw new UploadFileException(e);
		}

		try (InputStream is = urlObj.openStream()) {
			return is.readAllBytes();
		} catch (IOException e) {
			throw new UploadFileException(urlObj.toExternalForm(), e);
		}
	}
}