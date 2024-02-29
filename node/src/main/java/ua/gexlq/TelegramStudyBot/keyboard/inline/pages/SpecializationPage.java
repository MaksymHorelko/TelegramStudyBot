package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@Component
public class SpecializationPage extends InlineKeyboardFactory{

	
	public SpecializationPage(MessageUtils messageUtils) {
		super(messageUtils);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT [FIRST LAUNCH] 
	// || 
	// MAIN MENU -> SETTINGS MENU -> CHANGE DATA -> CHANGE SPECIALIZATION
	public InlineKeyboardMarkup createSpecializationPage(String faculty, String language) {
		String spec = "specialization" + "." + faculty;

		List<String> specializationCallBackData = new ArrayList<>();
		List<String> specializationName = new ArrayList<>();

		for (String code : createList(spec)) {
			specializationName.add(getButtonTextByCode(code, language));
		}

		for (String name : specializationName) {
			specializationCallBackData.add(CallBackDataTypes.SET_USER_SPECIALIZATION_TO + POINTER + name);
		}

		return createPage(specializationName, specializationCallBackData, language, false, false);
	}

}
