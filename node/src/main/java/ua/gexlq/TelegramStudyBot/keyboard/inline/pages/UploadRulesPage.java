package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageLoader;

@Component
public class UploadRulesPage extends InlineKeyboardFactory{
	
	public UploadRulesPage(MessageLoader messageLoader) {
		super(messageLoader);
	}

	public InlineKeyboardMarkup createUploadRulesPage(String language) {

		List<String> getRulesName = new ArrayList<>();
		List<String> getRulesCallBackData = new ArrayList<>();

		getRulesName.add(getButtonTextByCode("menu.inline.moreInfo", language));
		getRulesCallBackData.add(CallBackDataTypes.UPLOAD_RULES + POINTER);

		return createPage(getRulesName, getRulesCallBackData, language, false, false);
	}
}
