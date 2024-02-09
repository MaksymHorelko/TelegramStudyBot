package ua.gexlq.TelegramStudyBot.process.callBackData.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.entity.UpcomingDocumentRating;
import ua.gexlq.TelegramStudyBot.keyboard.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callBackData.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callBackData.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessUploadCallBackData implements ProcessCallBackDataByState {
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
		String selectedWork = callBackData.substring(callBackData.lastIndexOf(">") + 1);

		var user = appUserDAO.findUserByTelegramUserId(chatId);

		var documentRating = new UpcomingDocumentRating();
		documentRating.setWorkCode(selectedWork);
		user.setUpcomingDocumentRating(documentRating);
		appUserDAO.save(user);

		String rateContent = documentRating.getRateContent();
		String rateImpl = documentRating.getRateImplementaion();
		String rateMark = documentRating.getRateMark();

		String responseText = messageUtils.getAnswerTextByCode(MESSAGE_RATE_WORK, language);
		response = messageUtils.createEditMessageWithText(update,
				responseText.replace("{0}", rateContent).replace("{1}", rateImpl).replace("{2}", rateMark));
		response.setReplyMarkup(inlineKeyboardFactory.createRateContentPage(language, selectedWork));

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.UPLOAD.toString());
	}

}
