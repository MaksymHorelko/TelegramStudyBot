package ua.gexlq.TelegramStudyBot.process.callBackData.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.keyboard.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callBackData.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callBackData.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessWorkTypeCallBackData implements ProcessCallBackDataByState {
	private final InlineKeyboardFactory inlineKeyboardFactory;
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;

	private final String MESSAGE_PICK_WORK = "message.pick.work";


	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();
		String language = userInfo.getUserLanguage(chatId);

		EditMessageText response;

		String subject = callBackData.substring(callBackData.indexOf(">") + 1, callBackData.lastIndexOf("."));
		String workType = callBackData.substring(callBackData.lastIndexOf(".") + 1);

		response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_PICK_WORK);
		response.setReplyMarkup(inlineKeyboardFactory.createPickWorkPage(subject, workType, language));

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.SELECT_WORK_TYPE.toString());
	}

}
