package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.dao.AppSoftwareDAO;
import ua.gexlq.TelegramStudyBot.entity.AppSoftware;
import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@Component
public class SoftwarePage extends InlineKeyboardFactory {

	private final AppSoftwareDAO appSoftwareDAO;

	public SoftwarePage(MessageUtils messageUtils, AppSoftwareDAO appSoftwareDAO) {
		super(messageUtils);
		this.appSoftwareDAO = appSoftwareDAO;
	}

	// MAIN MENU -> SOFTWARE_MENU
	public InlineKeyboardMarkup createSoftwarePage() {
		List<String> softwareNames = new ArrayList<>();
		List<String> softwareCallBackData = new ArrayList<>();

		List<AppSoftware> software = appSoftwareDAO.findAll();

		for (AppSoftware currentSoftware : software) {
			softwareNames.add(
					currentSoftware.getSoftwareName().substring(0, currentSoftware.getSoftwareName().indexOf(".")));
			softwareCallBackData.add(CallBackDataTypes.DOWNLOAD_SOFTWARE + POINTER + currentSoftware.getSoftwareName());
		}

		return createPage(softwareNames, softwareCallBackData, defaultLanguage.toString(), false, false);
	}
}
