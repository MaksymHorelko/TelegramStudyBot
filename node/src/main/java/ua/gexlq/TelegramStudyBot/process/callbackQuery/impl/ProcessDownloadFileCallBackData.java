package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppDocumentDAO;
import ua.gexlq.TelegramStudyBot.entity.AppDocument;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@RequiredArgsConstructor
@Component
public class ProcessDownloadFileCallBackData {
	private final AppDocumentDAO appDocumentDAO;

	private final MessageUtils messageUtils;

	@Value("${folder.folderGroupId}")
	private String folderChatId;

	public ForwardMessage handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();
		String workCode = callBackData.substring(callBackData.indexOf(">") + 1, callBackData.lastIndexOf("."));
		String i = callBackData.substring(callBackData.lastIndexOf(".") + 1);

		ForwardMessage response;

		List<AppDocument> appDocuments = appDocumentDAO.findDocumentByWorkCode(workCode);
		var appDocument = appDocuments.get(Integer.valueOf(i) - 1);

		long forwardMessageId = appDocument.getMessageIdInFolder();
		response = messageUtils.createForwardMessageDocument(Long.valueOf(folderChatId), chatId, forwardMessageId);

		return response;
	}

	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.DOWNLOAD_FILE.toString());
	}

}
