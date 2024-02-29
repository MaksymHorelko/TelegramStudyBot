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
public class WorkOptionPage extends InlineKeyboardFactory {

	public WorkOptionPage(MessageUtils messageUtils) {
		super(messageUtils);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT -> CHOOSE WORKTYPE -> WORK ->
	// CHOOSE VARIANT -> CHOOSE OPTION (DOWNLOAD OR UPLOAD)
	public InlineKeyboardMarkup createWorkOptionPage(String selectedWork, String language) {
		String upload = "menu.inline.upload";
		String download = "menu.inline.download";

		List<String> options = Arrays.asList(CallBackDataTypes.UPLOAD.toString(),
				CallBackDataTypes.DOWNLOAD.toString());

		List<String> optionCallBackData = new ArrayList<>();

		List<String> optionName = new ArrayList<>();

		optionName.add(getButtonTextByCode(upload, language));
		optionName.add(getButtonTextByCode(download, language));

		for (String option : options) {
			optionCallBackData.add(option + POINTER + selectedWork);
		}

		backButtonCallBackData = CallBackDataTypes.GO_BACK_TO + POINTER + CallBackDataTypes.SELECT_VARIANT + POINTER
				+ selectedWork;

		return createPage(optionName, optionCallBackData, language, false, true);
	}

}
