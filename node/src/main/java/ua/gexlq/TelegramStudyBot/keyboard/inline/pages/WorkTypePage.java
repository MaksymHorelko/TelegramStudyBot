package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@Component
public class WorkTypePage extends InlineKeyboardFactory {

	public WorkTypePage(MessageUtils messageUtils) {
		super(messageUtils);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT-> CHOOSE WORKTYPE (TEST, LAB,
	// PRAC..)
	public InlineKeyboardMarkup createWorkTypePage(String subject, String language) {
		String base = "workTypes" + ".";
		List<String> codeToCategory = Arrays.asList("1", "2", "3", "4", "5", "6");

		List<String> categoryName = new ArrayList<>();
		List<String> categoryCallBackData = new ArrayList<>();

		for (String code : codeToCategory) {
			if(messageUtils.getAnswerTextByCode("works."+subject + "." + code, defaultLanguage).equals("error"))
				continue;
			categoryName.add(getButtonTextByCode(base + code + ".name", language));
			categoryCallBackData.add(CallBackDataTypes.SELECT_WORK_TYPE + POINTER + subject + "." + code);
		}

		backButtonCallBackData = CallBackDataTypes.GO_BACK_TO + POINTER + CallBackDataTypes.SELECT_SUBJECT_PAGE;

		return createPage(categoryName, categoryCallBackData, language, false, true);
	}
}
