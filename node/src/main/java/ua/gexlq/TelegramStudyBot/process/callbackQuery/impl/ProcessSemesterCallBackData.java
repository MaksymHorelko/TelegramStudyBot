package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.entity.enums.UserState;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.SubjectPage;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessSemesterCallBackData implements ProcessCallBackDataByState {
	private final AppUserDAO appUserDAO;
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;

	private final SubjectPage subjectPage;
	
	private final String MESSAGE_WORKS_SUBJECT = "message.pick.subject";
	private final String MESSAGE_IS_CORRECT = "message.isCorrect";
	private final String MESSAGE_SUCCESS = "message.success";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();
		String language = userInfo.getUserLanguage(chatId);

		EditMessageText response;
		var user = appUserDAO.findUserByTelegramUserId(chatId);

		String semester = callBackData.substring(callBackData.indexOf(">") + 1);
		
		user.setCurrentActiveMessageId(String.valueOf(update.getCallbackQuery().getMessage().getMessageId()));
		user.setSemester(semester);
		appUserDAO.save(user);

		if (user.getUserState().equals(UserState.MAIN_STATE)) {
			response = messageUtils.createEditMessageWithText(update,
					messageUtils.getAnswerTextByCode(MESSAGE_IS_CORRECT, language) + "\n\n"
							+ messageUtils.getAnswerTextByCode(MESSAGE_WORKS_SUBJECT, language));
			response.setReplyMarkup(subjectPage.createSubjectPage(user.getFaculty(), user.getSpecialization(),
					user.getSemester(), language));
		} else {
			response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_SUCCESS);
		}

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.SET_USER_SEMESTER_TO.toString());
	}

}
