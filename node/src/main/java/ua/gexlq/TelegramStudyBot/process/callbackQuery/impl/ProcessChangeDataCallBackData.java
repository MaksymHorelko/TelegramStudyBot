package ua.gexlq.TelegramStudyBot.process.callbackQuery.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.FacultyPage;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.SemesterPage;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.SpecializationPage;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.ProcessCallBackDataByState;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessChangeDataCallBackData implements ProcessCallBackDataByState {
	private final MessageUtils messageUtils;
	private final UserInfo userInfo;
	
	private final FacultyPage facultyPage;
	private final SpecializationPage specializationPage;
	private final SemesterPage semesterPage;

	private final String MESSAGE_PICK_FACULTY = "message.pick.faculty";
	private final String MESSAGE_PICK_SPECIALIZATION = "message.pick.specialization";
	private final String MESSAGE_PICK_SEMESTER = "message.pick.semester";

	@Override
	public EditMessageText handle(Update update) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		String language = userInfo.getUserLanguage(chatId);

		EditMessageText response;

		if (isChangeFaculty(update)) {
			response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_PICK_FACULTY);
			response.setReplyMarkup(facultyPage.createFacultyPage(language));
		}

		else if (isChangeSpecialization(update)) {
			String faculty = userInfo.getUserFaculty(chatId);

			response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_PICK_SPECIALIZATION);
			response.setReplyMarkup(specializationPage.createSpecializationPage(faculty, language));

		}

		else if (isChangeSemester(update)) {
			response = messageUtils.createEditMessageWithAnswerCode(update, MESSAGE_PICK_SEMESTER);
			response.setReplyMarkup(semesterPage.createSemesterPage(language));
		}

		else {
			response = messageUtils.createEditMessageWithText(update, "woi");
		}

		return response;
	}

	@Override
	public boolean isUpdateSupportedState(Update update) {
		return isChangeFaculty(update) || isChangeSpecialization(update) || isChangeSemester(update);
	}

	private boolean isChangeFaculty(Update update) {
		return update.getCallbackQuery().getData().equals(CallBackDataTypes.CHANGE_FACULTY.toString());
	}

	private boolean isChangeSpecialization(Update update) {
		return update.getCallbackQuery().getData().equals(CallBackDataTypes.CHANGE_SPECIALIZATION.toString());
	}

	private boolean isChangeSemester(Update update) {
		return update.getCallbackQuery().getData().equals(CallBackDataTypes.CHANGE_SEMESTER.toString());
	}

}
