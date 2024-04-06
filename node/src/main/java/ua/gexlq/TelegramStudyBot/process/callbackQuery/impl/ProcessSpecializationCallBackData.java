package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.entity.enums.UserState;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.SemesterPage;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessSpecializationCallBackData implements ProcessCallBackDataByState {
	private final AppUserDAO appUserDAO;
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;

	private final SemesterPage semesterPage;
	
	private final String PICK_SEMESTER = "message.pick.semester";
	private final String MESSAGE_SUCCESS = "message.success";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();

		EditMessageText response;
		var user = appUserDAO.findUserByTelegramUserId(chatId);

		String specialization = callBackData.substring(callBackData.indexOf(">") + 1);

		user.setCurrentActiveMessageId(String.valueOf(update.getCallbackQuery().getMessage().getMessageId()));
		user.setSpecialization(specialization);
		appUserDAO.save(user);

		if (userInfo.isUserSemesterSet(chatId) && user.getUserState().equals(UserState.MAIN_STATE)) {
			response = messageUtils.createEditMessageWithAnswerCode(update, PICK_SEMESTER);
			response.setReplyMarkup(semesterPage.createSemesterPage());
		} else {
			response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_SUCCESS);
		}
		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.SET_USER_SPECIALIZATION_TO.toString());
	}

}
