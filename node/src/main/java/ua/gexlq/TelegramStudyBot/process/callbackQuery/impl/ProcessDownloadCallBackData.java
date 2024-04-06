package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppDocumentDAO;
import ua.gexlq.TelegramStudyBot.entity.AppDocument;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.DownloadFilePage;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessDownloadCallBackData implements ProcessCallBackDataByState {
	private final AppDocumentDAO appDocumentDAO;

	private final UserInfo userInfo;
	private final MessageUtils messageUtils;

	private final DownloadFilePage downloadFilePage;

	private final String MESSAGE_WORKS_NOW_WORKS_FOUND = "message.works.notFound";
	private final String MESSAGE_PICK_WORK = "message.pick.work";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();

		String code = callBackData.substring(callBackData.indexOf(">") + 1);
		String language = userInfo.getUserLanguage(chatId);

		EditMessageText response;

		List<AppDocument> appDocuments = appDocumentDAO.findDocumentByWorkCode(code);
		int size = appDocuments.size();

		if (size == 0) {
			response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_WORKS_NOW_WORKS_FOUND);

		} else {
			response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_PICK_WORK);
			response.setReplyMarkup(downloadFilePage.createPickDownloadFilePage(code, language, size));
		}
		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.DOWNLOAD.toString());
	}

}
