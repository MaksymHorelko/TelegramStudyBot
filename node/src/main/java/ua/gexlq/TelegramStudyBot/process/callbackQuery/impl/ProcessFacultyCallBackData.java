package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.SpecializationPage;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessFacultyCallBackData implements ProcessCallBackDataByState {

	private final AppUserDAO appUserDAO;
	private final SpecializationPage page;
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;

	private final String PICK_SPECIALIZATION = "message.pick.specialization";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();
		String language = userInfo.getUserLanguage(chatId);

		EditMessageText response;
		var user = appUserDAO.findUserByTelegramUserId(chatId);

		String faculty = callBackData.substring(callBackData.indexOf(">") + 1);
		
		user.setCurrentActiveMessageId(String.valueOf(update.getCallbackQuery().getMessage().getMessageId()));
		user.setFaculty(faculty);
		appUserDAO.save(user);

		response = messageUtils.createEditMessageWithAnswerCode(update, PICK_SPECIALIZATION);
		response.setReplyMarkup(page.createSpecializationPage(faculty, language));

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.SET_USER_FACULTY_TO.toString());
	}
}
