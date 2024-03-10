package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageLoader;

@Component
public class WorkCorrectionPage extends InlineKeyboardFactory {

	public WorkCorrectionPage(MessageLoader messageLoader) {
		super(messageLoader);
	}

	// MAIN MENU -> WORK MENU -> SELECT WORK -> CORRECTION PAGE
	public InlineKeyboardMarkup createWorkCorrectionPage(String workCode, String language) {

		List<String> rateName = new ArrayList<>();
		List<String> rateCallBackData = new ArrayList<>();

		nextButtonCallBackData = CallBackDataTypes.GO_NEXT_TO + POINTER + CallBackDataTypes.SELECT_OPTION + POINTER
				+ workCode;
		backButtonCallBackData = CallBackDataTypes.SELECT_WORK + POINTER
				+ workCode.substring(0, workCode.lastIndexOf("."));
		return createPage(rateName, rateCallBackData, language, true, true);
	}
}
