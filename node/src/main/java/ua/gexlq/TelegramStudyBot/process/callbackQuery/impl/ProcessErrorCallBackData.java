package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@RequiredArgsConstructor
@Component
public class ProcessErrorCallBackData implements ProcessCallBackDataByState {

	private final MessageUtils messageUtils;

	private String MESSAGE_ERROR = "message.error";

	@Override
	public EditMessageText handle(Update update) {

		var response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_ERROR);

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().equals(CallBackDataTypes.ERROR.toString());
	}

}
