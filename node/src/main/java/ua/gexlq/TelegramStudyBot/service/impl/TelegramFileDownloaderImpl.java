package ua.gexlq.TelegramStudyBot.service.impl;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.entity.DownloadedFile;
import ua.gexlq.TelegramStudyBot.exceptions.FileServiceException;
import ua.gexlq.TelegramStudyBot.file.DocumentBuilder;
import ua.gexlq.TelegramStudyBot.file.FileSaver;
import ua.gexlq.TelegramStudyBot.service.FileInfoService;
import ua.gexlq.TelegramStudyBot.service.FileDownloader;
import ua.gexlq.TelegramStudyBot.service.FileStorageService;
import ua.gexlq.TelegramStudyBot.utils.UserPermissionsService;

import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Service
public class TelegramFileDownloaderImpl implements FileDownloader {
	private final UserPermissionsService permissionsService;

	private final FileInfoService fileInfoService;
	private final FileStorageService fileStorageService;
	private final FileSaver fileSaver;
	private final DocumentBuilder documentBuilder;

	@Override
	public DownloadedFile downloadDocument(Update update) {
		var telegramMessage = update.getMessage();

		String fileId = telegramMessage.getDocument().getFileId();
		ResponseEntity<String> response = fileInfoService.getFilePath(fileId);

		if (response.getStatusCode() == HttpStatus.OK) {
			JSONObject jsonObject = new JSONObject(response.getBody());
			String filePath = String.valueOf(jsonObject.getJSONObject("result").getString("file_path"));
			byte[] fileInByte = fileStorageService.downloadFile(filePath);

			String fullFilePath = fileSaver.saveFileLocally(fileInByte,
					update.getMessage().getDocument().getMimeType());

			permissionsService.addNewUpload(update);

			return documentBuilder.buildDownloadedFile(update, fullFilePath);
		} else {
			throw new FileServiceException("Bad response from telegram service: " + response);
		}
	}
}
