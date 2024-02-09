package ua.gexlq.TelegramStudyBot.process.callBackData.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.process.callBackData.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callBackData.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@RequiredArgsConstructor
@Component
public class ProcessLanguageCallBackData implements ProcessCallBackDataByState {
	private final AppUserDAO appUserDAO;
	private final MessageUtils messageUtils;

	private final String MESSAGE_SUCCESS = "message.success";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();

		EditMessageText response;
		String newLanguage = callBackData.substring(callBackData.indexOf(">") + 1);

		var user = appUserDAO.findUserByTelegramUserId(chatId);
		user.setUserLanguage(newLanguage);
		appUserDAO.save(user);

		response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_SUCCESS);

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.CHANGE_USER_LANGUAGE_TO.toString());
	}

}
