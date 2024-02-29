package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@Component
public class RateCorrectionPage extends InlineKeyboardFactory{
	
	public RateCorrectionPage(MessageUtils messageUtils) {
		super(messageUtils);
	}

	// MAIN MENU -> WORK MENU -> SELECT WORK -> UPLOAD -> RATE CONTENT -> RATE IMPL
	// -> RATE MARK -> RATED CORRECTION
	public InlineKeyboardMarkup createRatedCorrectionPage(String language, String workCode) {

		List<String> rateName = new ArrayList<>();
		List<String> rateCallBackData = new ArrayList<>();

		nextButtonCallBackData = CallBackDataTypes.GO_NEXT_TO + POINTER + CallBackDataTypes.UPLOAD_FILE;
		backButtonCallBackData = CallBackDataTypes.GO_BACK_TO + POINTER + CallBackDataTypes.UPLOAD + POINTER + workCode;
		return createPage(rateName, rateCallBackData, language, true, true);
	}
}
