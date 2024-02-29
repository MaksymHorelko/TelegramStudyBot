package ua.gexlq.TelegramStudyBot.process.document.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.entity.DownloadedFile;
import ua.gexlq.TelegramStudyBot.exceptions.DocumentServiceException;
import ua.gexlq.TelegramStudyBot.exceptions.UnsafeFileException;
import ua.gexlq.TelegramStudyBot.service.FileService;
import ua.gexlq.TelegramStudyBot.utils.UserPermissionsService;

@RequiredArgsConstructor
@Component
public class ProcessDocument {

	private final UserPermissionsService permissionsService;

	private final FileService fileService;

	public DownloadedFile handle(Update update) {
		var downloadedDocument = fileService.downloadDocument(update);
		permissionsService.addNewUpload(update);

		String filePath = downloadedDocument.getFilePath();

		boolean isSafe = fileService.isFileSafe(filePath);
		permissionsService.setFileUploadPermission(update, false);

		if (isSafe) {
			return downloadedDocument;
		}

		else {
			handleUnsafeFile(update);
		}
		
		throw new DocumentServiceException("Something went wrong");
	}

	public boolean isSupportedType(Update update) {
		return update.getMessage().getChatId() > 0L && update.getMessage().hasDocument();
	}

	private void handleUnsafeFile(Update update) {
		permissionsService.addNewWarning(update);
		throw new UnsafeFileException("Received file contains viruses, author : " + update.getMessage().getChatId());
	}

}
