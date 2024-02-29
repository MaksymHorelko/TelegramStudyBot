package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.WorkOptionPage;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessWorkOptionCallBackData implements ProcessCallBackDataByState {
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;

	private final WorkOptionPage workOptionPage;

	private final String MESSAGE_PICK_OPTION = "message.pick.option";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();

		String workCode = callBackData.substring(callBackData.indexOf(">") + 1);

		String language = userInfo.getUserLanguage(chatId);
		EditMessageText response;

		response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_PICK_OPTION);
		response.setReplyMarkup(workOptionPage.createWorkOptionPage(workCode, language));

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.SELECT_OPTION.toString());
	}

}
