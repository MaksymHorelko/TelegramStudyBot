package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessUploadRulesCallBackData implements ProcessCallBackDataByState {
	private final AppUserDAO appUserDAO;
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;

	private final String MESSAGE_UPLOAD_MORE_INFO = "message.upload.moreInfo";


	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String language = userInfo.getUserLanguage(chatId);

		EditMessageText response;
		var user = appUserDAO.findUserByTelegramUserId(chatId);
		var permissions = user.getPermissions();
		permissions.setAbleToSendFile(true);
		user.setPermissions(permissions);
		appUserDAO.save(user);

		String moreInfo = messageUtils.getAnswerTextByCode(MESSAGE_UPLOAD_MORE_INFO, language);

		response = messageUtils.createEditMessageWithText(update, moreInfo);

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.UPLOAD_RULES.toString());
	}
}
