package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppDataDAO;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.entity.UpcomingDocument;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.RateContentPage;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessUploadCallBackData implements ProcessCallBackDataByState {
	private final AppUserDAO appUserDAO;
	private final AppDataDAO appDataDAO;
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;

	private final RateContentPage contentPage;

	private final String MESSAGE_RATE_WORK = "message.upload.rateWork";
	private final String MESSAGE_USER_NOT_TRUSTED = "message.userNotTrusted";
	private final String MESSAGE_WORK_UPLOADED_FILES_LIMIT_REACHED = "message.upload.filesLimitReached";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();
		String language = userInfo.getUserLanguage(chatId);

		EditMessageText response;
		String selectedWork = callBackData.substring(callBackData.lastIndexOf(">") + 1);

		var user = appUserDAO.findUserByTelegramUserId(chatId);

		if (!user.getPermissions().getTrusted()) {
			var isNotTrusted = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_USER_NOT_TRUSTED);
			return isNotTrusted;
		}

		if (user.getPermissions().getUploadedFilesToday() >= appDataDAO.getAppData().getMaxUploadedFilesPerDay()) {
			var limitReached = messageUtils.createEditMessageWithAnswerCode(update,
					MESSAGE_WORK_UPLOADED_FILES_LIMIT_REACHED);
			return limitReached;
		}

		var upcomingDocument = new UpcomingDocument();
		upcomingDocument.setWorkCode(selectedWork);
		user.setUpcomingDocument(upcomingDocument);
		appUserDAO.save(user);

		String rateContent = upcomingDocument.getRateContent();
		String rateImpl = upcomingDocument.getRateImplementaion();
		String rateMark = upcomingDocument.getRateMark();

		String responseText = messageUtils.getAnswerTextByCode(MESSAGE_RATE_WORK, language);
		response = messageUtils.createEditMessageWithText(update,
				responseText.replace("{0}", rateContent).replace("{1}", rateImpl).replace("{2}", rateMark));
		response.setReplyMarkup(contentPage.createRateContentPage(language, selectedWork));

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.UPLOAD.toString());
	}

}
