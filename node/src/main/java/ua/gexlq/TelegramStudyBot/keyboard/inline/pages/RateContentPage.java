package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageLoader;

@Component
public class RateContentPage extends InlineKeyboardFactory{
	
	public RateContentPage(MessageLoader messageLoader) {
		super(messageLoader);
	}

	// MAIN MENU -> WORK MENU -> SELECT WORK -> UPLOAD -> RATE CONTENT
	public InlineKeyboardMarkup createRateContentPage(String language, String selectedWork) {

		List<String> rateName = new ArrayList<>();
		List<String> rateCallBackData = new ArrayList<>();

		for (int i = 10; i < 110; i += 10)
			rateName.add(Integer.toString(i) + "%");

		for (String rate : rateName)
			rateCallBackData.add(CallBackDataTypes.RATE_CONTENT + POINTER + rate);

		backButtonCallBackData = CallBackDataTypes.GO_BACK_TO + POINTER + CallBackDataTypes.SELECT_OPTION + POINTER
				+ selectedWork;

		return createPage(rateName, rateCallBackData, language, false, true);
	}
}
