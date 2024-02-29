package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@Component
public class WorkVariantPage extends InlineKeyboardFactory {
	
	public WorkVariantPage(MessageUtils messageUtils) {
		super(messageUtils);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT -> CHOOSE WORKTYPE -> CHOOSE WORK -> CHOOSE VARIANT
	public InlineKeyboardMarkup createWorkVariantPage(String selectedWork, String language) {

		List<String> name = new ArrayList<>();
		List<String> callBackData = new ArrayList<>();

		for (int i = 1; i < maxVariants + 1; i++) {
			name.add(Integer.toString(i));
			callBackData.add(CallBackDataTypes.SELECT_VARIANT + POINTER + selectedWork + "." + Integer.toString(i));
		}

		backButtonCallBackData = CallBackDataTypes.GO_BACK_TO + POINTER + CallBackDataTypes.SELECT_WORK_TYPE + POINTER
				+ selectedWork.substring(0, selectedWork.lastIndexOf("."));

		return createPage(name, callBackData, language, false, true);
	}
}
