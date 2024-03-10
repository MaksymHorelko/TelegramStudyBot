package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageLoader;

@Component
public class SemesterPage extends InlineKeyboardFactory {
	
	public SemesterPage(MessageLoader messageLoader) {
		super(messageLoader);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT [FIRST LAUNCH] || MAIN MENU ->
	// SETTINGS MENU -> CHANGE DATA -> CHANGE SEMESTER
	public InlineKeyboardMarkup createSemesterPage(String language) {
		List<String> semesterName = new ArrayList<>();
		List<String> semesterCallBackData = new ArrayList<>();

		for (int i = 1; i < maxSemesters + 1; i++)
			semesterName.add(Integer.toString(i));

		for (String semester : semesterName)
			semesterCallBackData.add(CallBackDataTypes.SET_USER_SEMESTER_TO + POINTER + semester);

		return createPage(semesterName, semesterCallBackData, language, false, false);
	}
}
