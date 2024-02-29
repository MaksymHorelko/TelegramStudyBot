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
public class ChangeLanguagePage extends InlineKeyboardFactory {

	public ChangeLanguagePage(MessageUtils messageUtils) {
		super(messageUtils);
	}

	// MAIN MENU -> SETTINGS MENU -> CHANGE LANGUAGE
	public InlineKeyboardMarkup createChangeLanguagePage(String language) {
		String base = "languages" + ".";
		List<String> languages = Arrays.asList("ukrainian", "russian", "english");

		List<String> languageCallBackData = new ArrayList<>();
		List<String> languageName = new ArrayList<>();

		for (String lang : languages) {
			languageCallBackData.add(CallBackDataTypes.CHANGE_USER_LANGUAGE_TO + POINTER + lang);
		}

		for (String lang : languages) {
			languageName.add(getButtonTextByCode(base + lang + ".name", language));
		}

		return createPage(languageName, languageCallBackData, language, false, false);
	}
}
