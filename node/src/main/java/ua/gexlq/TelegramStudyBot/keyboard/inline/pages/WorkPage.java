package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@Component
public class WorkPage extends InlineKeyboardFactory {

	public WorkPage(MessageUtils messageUtils) {
		super(messageUtils);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT -> CHOOSE WORKTYPE -> CHOOSE WORK
	public InlineKeyboardMarkup createPickWorkPage(String subject, String workType, String language) {

		List<String> workNames = new ArrayList<>();
		List<String> callBackData = new ArrayList<>();

		String workTypeName = messageUtils.getAnswerTextByCode("workTypes" + "." + workType + "." + "abbr", language);

		try {
			int countWorks = Integer
					.parseInt(messageUtils.getAnswerTextByCode("works." + subject + "." + workType, language));

			for (int i = 1; i <= countWorks; i++) {
				workNames.add(workTypeName + " " + i);
				callBackData.add(CallBackDataTypes.SELECT_WORK + POINTER + subject + "." + workType + "." + i);
			}
		} catch (NumberFormatException e) {
			return null;
		}

		if (workNames.isEmpty()) {
			return null;
		}

		backButtonCallBackData = CallBackDataTypes.GO_BACK_TO + POINTER + CallBackDataTypes.SELECT_SUBJECT + POINTER
				+ subject;

		return createPage(workNames, callBackData, language, false, true);
	}
}
