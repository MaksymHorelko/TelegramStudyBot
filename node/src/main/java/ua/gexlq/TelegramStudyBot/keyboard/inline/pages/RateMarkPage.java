package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageLoader;

@Component
public class RateMarkPage extends InlineKeyboardFactory{
	
	public RateMarkPage(MessageLoader messageLoader) {
		super(messageLoader);
	}

	public InlineKeyboardMarkup createRateMarkPage() {

		List<String> rateName = new ArrayList<>();
		List<String> rateCallBackData = new ArrayList<>();

		for (int i = 10; i < 110; i += 10)
			rateName.add(Integer.toString(i) + "%");

		for (String rate : rateName)
			rateCallBackData.add(CallBackDataTypes.RATE_MARK + POINTER + rate);

		return createPage(rateName, rateCallBackData, "uk", false, false);
	}
}
