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
public class ProcessRateContentCallBackData implements ProcessCallBackDataByState {
	private final AppUserDAO appUserDAO;
	private final InlineKeyboardFactory inlineKeyboardFactory;
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;

	private final String MESSAGE_RATE_WORK = "message.upload.rateWork";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();
		String language = userInfo.getUserLanguage(chatId);

		EditMessageText response;

		var user = appUserDAO.findUserByTelegramUserId(chatId);
		var documentRating = user.getUpcomingDocumentRating();

		String rateContent = callBackData.substring(callBackData.indexOf(">") + 1);
		String rateImpl = documentRating.getRateImplementaion();
		String rateMark = documentRating.getRateMark();

		documentRating.setRateContent(rateContent);
		user.setUpcomingDocumentRating(documentRating);
		appUserDAO.save(user);

		String responseText = messageUtils.getAnswerTextByCode(MESSAGE_RATE_WORK, language);
		response = messageUtils.createEditMessageWithText(update,
				responseText.replace("{0}", rateContent).replace("{1}", rateImpl).replace("{2}", rateMark));
		response.setReplyMarkup(inlineKeyboardFactory.createRateImplementationPage());

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.RATE_CONTENT.toString());
	}

}
