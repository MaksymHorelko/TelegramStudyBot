package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.WorkVariantPage;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessWorkCallBackData implements ProcessCallBackDataByState {
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;

	private final WorkVariantPage variantPage;
	
	private final String MESSAGE_PICK_VARIANT = "message.pick.variant";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();
		String language = userInfo.getUserLanguage(chatId);

		EditMessageText response;

		String selectedWork = callBackData.substring(callBackData.indexOf(">") + 1);

		response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_PICK_VARIANT);
		response.setReplyMarkup(variantPage.createWorkVariantPage(selectedWork, language));

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.SELECT_WORK.toString());
	}

}
