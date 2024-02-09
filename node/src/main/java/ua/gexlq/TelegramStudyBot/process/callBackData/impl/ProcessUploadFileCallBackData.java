package ua.gexlq.TelegramStudyBot.process.callBackData.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.keyboard.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callBackData.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callBackData.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessUploadFileCallBackData implements ProcessCallBackDataByState {
	private final AppUserDAO appUserDAO;
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;
	private final InlineKeyboardFactory inlineKeyboardFactory;

	private final String MESSAGE_UPLOAD_READY_TO_SEND_WORK = "message.upload.readyToSendWork";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String language = userInfo.getUserLanguage(chatId);

		EditMessageText response;
		var user = appUserDAO.findUserByTelegramUserId(chatId);
		user.setIsReadyToSendFile(true);
		appUserDAO.save(user);

		response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_UPLOAD_READY_TO_SEND_WORK);
		response.setReplyMarkup(inlineKeyboardFactory.createUploadRulesPage(language));

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.UPLOAD_FILE.toString());
	}

}
