package ua.gexlq.TelegramStudyBot.process.text.impl;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppDataDAO;
import ua.gexlq.TelegramStudyBot.dao.AppDocumentDAO;
import ua.gexlq.TelegramStudyBot.dao.DownloadedFileDAO;
import ua.gexlq.TelegramStudyBot.entity.AppDocument;
import ua.gexlq.TelegramStudyBot.entity.enums.FileState;
import ua.gexlq.TelegramStudyBot.exceptions.CheckGroupException;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserPermissionsService;

@RequiredArgsConstructor
@Component
public class ProcessCheckGroup {

	// DAO
	private final AppDataDAO appDataDAO;
	private final AppDocumentDAO appDocumentDAO;
	private final DownloadedFileDAO downloadedFileDAO;
	private final UserPermissionsService permissionsService;

	// Utilities
	private final MessageUtils messageUtils;

	@Value("${folder.checkGroupId}")
	private String checkGroupId;

	@Value("${folder.folderGroupId}")
	private String folderGroupId;

	public ForwardMessage handle(Update update) {
		long chatId = update.getMessage().getChatId();

		if (!checkGroupId.equals(String.valueOf(chatId))) {
			throw new CheckGroupException("Telegram group " + chatId + " is not supported");
		}

		var appData = appDataDAO.getAppData();

		if (appData.getFileState().equals(FileState.NO_FILE_IN_QUEUE)) {
			throw new CheckGroupException("File not found in check queue");
		}

		String messageText = update.getMessage().getText();

		long currentMessageId = update.getMessage().getMessageId();

		boolean fileIsCorrect = messageText.equals("+") ? true : false;

		appData.setFileState(FileState.NO_FILE_IN_QUEUE);
		appDataDAO.save(appData);

		if (fileIsCorrect) {
			buildAppDocument();
			return createForwardMessageToFolder(currentMessageId);
		} else {
			permissionsService.addNewWarning(update);
			throw new CheckGroupException("File with messageId: " + currentMessageId + " is not correct");
		}

	}

	private ForwardMessage createForwardMessageToFolder(long currentMessageId) {
		long checkedFileMessageId = currentMessageId - 1;
		return messageUtils.createForwardMessageDocument(Long.valueOf(checkGroupId), Long.valueOf(folderGroupId),
				checkedFileMessageId);
	}

	private AppDocument buildAppDocument() {
		var appData = appDataDAO.getAppData();
		long fileOnCheck = appData.getFileOnCheck();

		var document = downloadedFileDAO.findDownloadedFileById(fileOnCheck);
		var documentMetadata = document.getDocumentMetadata();

		long messageId = getLastMessageIdInFolderChat();

		var appDocument = AppDocument.builder().documentMetadata(documentMetadata).messageIdInFolder(messageId).build();

		return appDocumentDAO.save(appDocument);
	}

	private long getLastMessageIdInFolderChat() {
		long messageId;
		var lastDocument = appDocumentDAO.findLastDocument();

		if (lastDocument == null) {
			var scan = new Scanner(System.in);
			System.out.println("Enter LAST_MESSAGE_ID in your 'document folder' CHAT: ");
			messageId = scan.nextLong();
			scan.close();
		}

		else {
			messageId = lastDocument.getMessageIdInFolder() + 1;
		}
		return messageId;
	}
}