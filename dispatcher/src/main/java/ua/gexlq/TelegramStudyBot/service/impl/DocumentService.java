package ua.gexlq.TelegramStudyBot.service.impl;

import java.io.File;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.DownloadedFileDAO;
import ua.gexlq.TelegramStudyBot.entity.DownloadedFile;

@RequiredArgsConstructor
@Service
public class DocumentService {
	
	private final DownloadedFileDAO downloadedFileDAO;
	
	@Value("${folder.tempGroupId}")
	private String tempFolder;
	
	public SendDocument createSendDocument(DownloadedFile file) {
		String filePath = file.getFilePath();

		var sendDocument = new SendDocument();

		sendDocument.setChatId(tempFolder);

		InputFile inputFile = new InputFile(new File(filePath));
		sendDocument.setDocument(inputFile);

		long nextTempId;
		var lastDownloadedFile = downloadedFileDAO.findLastDownloadedFile();
		if (lastDownloadedFile == null) {
			var scan = new Scanner(System.in);
			System.out.println("Enter LAST_MESSAGE_ID in your 'temp folder' CHAT: ");
			nextTempId = scan.nextLong() + 1;
			scan.close();

		} else {
			nextTempId = lastDownloadedFile.getMessageIdInTemp() + 1;
		}

		file.setMessageIdInTemp(nextTempId);
		downloadedFileDAO.save(file);
		
		return sendDocument;
	}
}
