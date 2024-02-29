package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@Component
public class FacultyPage extends InlineKeyboardFactory {

	public FacultyPage(MessageUtils messageUtils) {
		super(messageUtils);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT [FIRST LAUNCH] || MAIN MENU ->
	// SETTINGS MENU -> CHANGE DATA -> CHANGE FACULTY
	public InlineKeyboardMarkup createFacultyPage(String language) {
		String faculty = "faculty";

		List<String> facultyCallBackData = new ArrayList<>();
		List<String> facultyNames = new ArrayList<>();

		int i = 1;
		for (String code : createList(faculty)) {
			facultyNames.add(getButtonTextByCode(code, language));
			facultyCallBackData.add(CallBackDataTypes.SET_USER_FACULTY_TO + POINTER + i);
			i++;
		}

		return createPage(facultyNames, facultyCallBackData, language, false, false);
	}
}
