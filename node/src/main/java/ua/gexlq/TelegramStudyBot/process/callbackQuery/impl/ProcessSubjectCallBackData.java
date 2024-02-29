package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.WorkTypePage;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessSubjectCallBackData implements ProcessCallBackDataByState {
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;

	private final WorkTypePage workTypePage;
	
	private final String MESSAGE_PICK_WORK_TYPE = "message.pick.workType";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();
		String language = userInfo.getUserLanguage(chatId);

		EditMessageText response;

		String subject = callBackData.substring(callBackData.indexOf(">") + 1);

		response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_PICK_WORK_TYPE);
		response.setReplyMarkup(workTypePage.createWorkTypePage(subject, language));

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.SELECT_SUBJECT.toString());
	}
}
