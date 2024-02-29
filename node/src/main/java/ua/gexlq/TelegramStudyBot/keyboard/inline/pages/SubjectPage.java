package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@Component
public class SubjectPage extends InlineKeyboardFactory {

	public SubjectPage(MessageUtils messageUtils) {
		super(messageUtils);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT
	public InlineKeyboardMarkup createSubjectPage(String faculty, String specialization, String semester,
			String language) {

		String base = "subjects." + faculty + "." + specialization + "." + semester;

		List<String> subjectCallBackData = createList(base);

		if (subjectCallBackData == null) {
			return null;
		}

		List<String> subjectNames = new ArrayList<>();

		for (String code : subjectCallBackData) {
			subjectNames.add(getButtonTextByCode(code, language));
		}

		for (int i = 0; i < subjectCallBackData.size(); i++) {
			subjectCallBackData.set(i,
					subjectCallBackData.get(i).replace("subjects.", CallBackDataTypes.SELECT_SUBJECT + POINTER));
		}

		return createPage(subjectNames, subjectCallBackData, language, false, false);
	}
}
