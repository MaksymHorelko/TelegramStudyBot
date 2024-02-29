package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@Component
public class DownloadFilePage extends InlineKeyboardFactory {

	public DownloadFilePage(MessageUtils messageUtils) {
		super(messageUtils);
	}

	// MAIN MENU -> WORK MENU -> SELECT WORK -> DOWNLOAD
	public InlineKeyboardMarkup createPickDownloadFilePage(String workCode, int nums, String language) {

		List<String> name = new ArrayList<>();
		List<String> callBackData = new ArrayList<>();

		for (int i = 1; i < nums + 1; i++) {
			name.add(String.valueOf(i));
			callBackData.add(CallBackDataTypes.DOWNLOAD_FILE + POINTER + workCode + "." + i);
		}

		backButtonCallBackData = CallBackDataTypes.GO_BACK_TO + POINTER + CallBackDataTypes.SELECT_OPTION + POINTER
				+ workCode;

		return createPage(name, callBackData, language, false, true);
	}
}
