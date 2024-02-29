package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.RateCorrectionPage;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessRateMarkCallBackData implements ProcessCallBackDataByState {

	private final AppUserDAO appUserDAO;
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;

	private final RateCorrectionPage rateCorrectionPage;
	
	private final String MESSAGE_RATE_WORK = "message.upload.rateWork";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();
		String language = userInfo.getUserLanguage(chatId);

		EditMessageText response;

		var user = appUserDAO.findUserByTelegramUserId(chatId);
		var upcomingDocument = user.getUpcomingDocument();

		String selectedWork = upcomingDocument.getWorkCode();
		String rateContent = upcomingDocument.getRateContent();
		String rateImpl = upcomingDocument.getRateImplementaion();
		String rateMark = callBackData.substring(callBackData.indexOf(">") + 1);

		upcomingDocument.setRateMark(rateMark);
		user.setUpcomingDocument(upcomingDocument);
		appUserDAO.save(user);

		String responseText = messageUtils.getAnswerTextByCode(MESSAGE_RATE_WORK, language);
		response = messageUtils.createEditMessageWithText(update,
				responseText.replace("{0}", rateContent).replace("{1}", rateImpl).replace("{2}", rateMark));

		response.setReplyMarkup(rateCorrectionPage.createRatedCorrectionPage(language, selectedWork));

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains("[RATE_MARK]");
	}
}
