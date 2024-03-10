package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageLoader;

@Component
public class ChangeDataPage extends InlineKeyboardFactory {
	
	public ChangeDataPage(MessageLoader messageLoader) {
		super(messageLoader);
	}

	// MAIN MENU -> SETTINGS MENU -> CHANGE DATA
	public InlineKeyboardMarkup createChangeDataPage(String language) {

		List<String> dataCallBackData = Arrays.asList(CallBackDataTypes.CHANGE_FACULTY.toString(),
				CallBackDataTypes.CHANGE_SPECIALIZATION.toString(), CallBackDataTypes.CHANGE_SEMESTER.toString());

		List<String> dataName = new ArrayList<>();

		for (String code : Arrays.asList("faculty.name", "specialization.name", "semester.name")) {
			dataName.add(getButtonTextByCode(code, language));
		}

		return createPage(dataName, dataCallBackData, language, false, false);
	}
}
