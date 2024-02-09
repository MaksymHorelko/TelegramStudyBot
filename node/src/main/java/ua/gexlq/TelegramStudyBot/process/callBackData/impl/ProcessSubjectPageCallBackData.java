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
public class ProcessSubjectPageCallBackData implements ProcessCallBackDataByState {
	private final InlineKeyboardFactory inlineKeyboardFactory;
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;
	private final AppUserDAO appUserDAO;

	private final String MESSAGE_WORK_SUBJECT = "message.works.subject";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();

		String faculty = userInfo.getUserFaculty(chatId);
		String specialization = userInfo.getUserSpecialization(chatId);
		String semester = userInfo.getUserSemester(chatId);

		String language = userInfo.getUserLanguage(chatId);

		EditMessageText response;

		response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_WORK_SUBJECT);
		response.setReplyMarkup(inlineKeyboardFactory.createSubjectPage(faculty, specialization, semester, language));

		var user = appUserDAO.findUserByTelegramUserId(update.getCallbackQuery().getFrom().getId());
		user.setCurrentActiveMessageId(String.valueOf(update.getCallbackQuery().getMessage().getMessageId()));
		appUserDAO.save(user);

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.SELECT_SUBJECT_PAGE.toString());
	}

}
