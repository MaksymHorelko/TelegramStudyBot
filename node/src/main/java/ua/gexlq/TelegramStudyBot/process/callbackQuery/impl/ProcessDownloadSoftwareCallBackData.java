package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppSoftwareDAO;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@RequiredArgsConstructor
@Component
public class ProcessDownloadSoftwareCallBackData {

	private final AppSoftwareDAO appSoftwareDAO;
	
	private final MessageUtils messageUtils;

	@Value("${folder.softwareGroupId}")
	private String folderSoftwareId;
	
	public ForwardMessage handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();
		String fileName = callBackData.substring(callBackData.indexOf(">") + 1);

		var appSoftware = appSoftwareDAO.findBySoftwareName(fileName);

		long forwardMessageId = appSoftware.getMessageIdInSoftwareFolder();
		var response = messageUtils.createForwardMessageDocument(Long.valueOf(folderSoftwareId), chatId, forwardMessageId);

		return response;
	}

	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.DOWNLOAD_SOFTWARE.toString());
	}
	
}
