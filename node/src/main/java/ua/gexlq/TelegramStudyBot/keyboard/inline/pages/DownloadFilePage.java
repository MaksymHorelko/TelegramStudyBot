package ua.gexlq.TelegramStudyBot.keyboard.inline.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import ua.gexlq.TelegramStudyBot.dao.AppDocumentDAO;
import ua.gexlq.TelegramStudyBot.entity.AppDocument;
import ua.gexlq.TelegramStudyBot.keyboard.inline.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageLoader;

@Component
public class DownloadFilePage extends InlineKeyboardFactory {

	private final AppDocumentDAO appDocumentDAO;

	public DownloadFilePage(MessageLoader messageLoader, AppDocumentDAO appDocumentDAO) {
		super(messageLoader);
		this.appDocumentDAO = appDocumentDAO;
	}

	// MAIN MENU -> WORK MENU -> SELECT WORK -> DOWNLOAD
	public InlineKeyboardMarkup createPickDownloadFilePage(String language, String workCode, int nums) {

		List<String> name = new ArrayList<>();
		List<String> callBackData = new ArrayList<>();

		List<AppDocument> appDocuments = appDocumentDAO.findDocumentByWorkCode(workCode);

		int i = 0;
		for (AppDocument document : appDocuments) {
			name.add(createDocumentInfoText(document, i));
			callBackData.add(CallBackDataTypes.DOWNLOAD_FILE + POINTER + workCode + "." + i);
			i++;
		}

		backButtonCallBackData = CallBackDataTypes.GO_BACK_TO + POINTER + CallBackDataTypes.SELECT_OPTION + POINTER
				+ workCode;

		return createPage(name, callBackData, language, false, true);
	}

	private String createDocumentInfoText(AppDocument document, int number) {
		var documentMetadata = document.getDocumentMetadata();
		var mimeType = documentMetadata.getMimeType();
		var fileSize = documentMetadata.getFileSize();
		var rateContent = documentMetadata.getRateContent();
		var rateImpl = documentMetadata.getRateImplementaion();
		var rateMark = documentMetadata.getRateMark();

		String button = number + "." + mimeType + "\n" + fileSize + "\n" + rateContent + " | " + rateImpl + " | "
				+ rateMark;
		return button;
	}
}
