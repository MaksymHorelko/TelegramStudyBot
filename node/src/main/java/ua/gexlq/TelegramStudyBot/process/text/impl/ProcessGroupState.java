package ua.gexlq.TelegramStudyBot.process.text.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppDataDAO;
import ua.gexlq.TelegramStudyBot.dao.AppDocumentDAO;
import ua.gexlq.TelegramStudyBot.dao.DownloadedFileDAO;
import ua.gexlq.TelegramStudyBot.entity.AppDocument;
import ua.gexlq.TelegramStudyBot.entity.enums.UserState;
import ua.gexlq.TelegramStudyBot.process.text.ProcessMessageByUserState;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@RequiredArgsConstructor
@Component
public class ProcessGroupState implements ProcessMessageByUserState {

	@Value("${folder.checkGroupId}")
	private String checkGroupId;

	@Value("${folder.folderGroupId}")
	private String folderGroupId;

	private final AppDataDAO appDataDAO;
	private final AppDocumentDAO appDocumentDAO;
	private final DownloadedFileDAO downloadedFileDAO;

	private final MessageUtils messageUtils;

	private final String MESSAGE_BOT_DOESNT_SUPPRORT_GROUPS = "message.botDoesntSupportGroups";

	@Override
	public SendMessage handle(Update update) {
		long chatId = update.getMessage().getChatId();
		Integer currentMessageId = update.getMessage().getMessageId();

		if (!checkGroupId.equals(String.valueOf(chatId))) {
			var send = new SendMessage();
			send = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_BOT_DOESNT_SUPPRORT_GROUPS);
			return send;
		}

		boolean correct = update.getMessage().getText().equals("+") ? true : false;

		if (correct) {
			var forwardDocToFolder = messageUtils.createForwardMessageDocument(checkGroupId, folderGroupId,
					currentMessageId - 1);

			var appDocument = buildAppDocument();

			return null;
		}

		// ELSE
		// TODO RECEIVED FILE IS TRASH

		return null;
	}

	private AppDocument buildAppDocument() {
		var appData = appDataDAO.getAppData();
		long fileOnCheck = appData.getFileOnCheck();

		var document = downloadedFileDAO.getById(fileOnCheck);
		var documentMetadata = document.getDocumentMetadata();

		long messageId = appData.getLastFolderMessageId() + 1;
		appData.setLastFolderMessageId(messageId);
		appDataDAO.save(appData);

		var appDocument = AppDocument.builder().documentMetadata(documentMetadata).messageIdInFolder(messageId).build();

		return appDocumentDAO.save(appDocument);
	}

	@Override
	public UserState getSupportedState() {
		return UserState.GROUP_STATE;
	}

}