package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.WorkCorrectionPage;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.ParseWorkCode;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessVariantCallBackData implements ProcessCallBackDataByState {
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;

	private final ParseWorkCode parseCode;
	private final WorkCorrectionPage correctionPage;

	private final String MESSAGE_WORKS_IS_DATA_CORRECT = "message.works.isDataCorrect";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String callBackData = update.getCallbackQuery().getData();
		String language = userInfo.getUserLanguage(chatId);

		EditMessageText response;

		String fullWorkCode = callBackData.substring(callBackData.indexOf(">") + 1);

		String facultyCode = parseCode.getFacultyByWorkCode(fullWorkCode);
		String specializationCode = parseCode.getSpecializationByWorkCode(fullWorkCode);
		String semesterCode = parseCode.getSemesterByWorkCode(fullWorkCode);
		String subjectCode = parseCode.getSubjectByWorkCode(fullWorkCode);
		String workTypeCode = parseCode.getWorkTypeByWorkCode(fullWorkCode);
		String workCode = parseCode.getWorkByWorkCode(fullWorkCode);
		String variantCode = parseCode.getWorkVariantByWorkCode(fullWorkCode);

		String baseCode = facultyCode + "." + specializationCode + "." + semesterCode;

		String subject = messageUtils.getAnswerTextByCode("subjects." + baseCode + "." + subjectCode, language);

		String work = messageUtils.getAnswerTextByCode("workTypes." + workTypeCode + ".abbr", language) + " "
				+ workCode;

		String messageText = messageUtils.getAnswerTextByCode(MESSAGE_WORKS_IS_DATA_CORRECT, language)
				.replace("{subject}", subject).replace("{work}", work).replace("{variant}", variantCode);

		response = messageUtils.createEditMessageWithText(update, messageText);
		response.setReplyMarkup(correctionPage.createWorkCorrectionPage(fullWorkCode, language));

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return update.getCallbackQuery().getData().contains(CallBackDataTypes.SELECT_VARIANT.toString());
	}

}
