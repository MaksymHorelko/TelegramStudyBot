package ua.gexlq.TelegramStudyBot.keyboard.inline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.entity.enums.Languages;
import ua.gexlq.TelegramStudyBot.utils.MessageLoader;

@RequiredArgsConstructor
@Component
public abstract class InlineKeyboardFactory {

	protected final MessageLoader messageLoader;

	protected final int maxSemesters = 10;

	protected final int maxVariants = 31;

	protected String nextButtonCallBackData;

	protected String backButtonCallBackData;

	protected Languages defaultLanguage = Languages.UKRANIAN;

	protected final String POINTER = "->";

	// Common method to get text by code
	protected String getButtonTextByCode(String code, String language) {
		return messageLoader.getTextByCode(code, language);
	}

	// Common method to create button
	protected List<InlineKeyboardButton> createButton(String buttonText, String callbackData, String language) {
		var button = new InlineKeyboardButton();
		button.setText(buttonText);
		button.setCallbackData(callbackData);
		return Collections.singletonList(button);
	}

	// Common method to create next button
	protected List<InlineKeyboardButton> createNextButton(String language) {
		return createButton(getButtonTextByCode("menu.next", language), nextButtonCallBackData, language);
	}

	// Common method to create back button
	protected List<InlineKeyboardButton> createBackButton(String language) {
		return createButton(getButtonTextByCode("menu.back", language), backButtonCallBackData, language);
	}

	// Common method to create row
	protected List<InlineKeyboardButton> createRow(List<String> names, List<String> callbackData, String language,
			int startIdx, int colsPerRow) {
		List<InlineKeyboardButton> row = new ArrayList<>();

		for (int i = startIdx; i < startIdx + colsPerRow && i < names.size(); i++) {
			row.addAll(createButton(names.get(i), callbackData.get(i), language));
		}

		return row;
	}

	// Common method to create page
	protected InlineKeyboardMarkup createPage(List<String> names, List<String> callbackData, String language,
			boolean nextButton, boolean backButton) {
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

		int colsPerRow = 2;
		if (names.size() >= 15) {
			colsPerRow = 3;
		}
		if (names.size() >= 24) {
			colsPerRow = 4;
		}

		for (int i = 0; i < names.size(); i += colsPerRow) {
			rowsInLine.add(createRow(names, callbackData, language, i, colsPerRow));
		}

		if (backButton)
			rowsInLine.add(createBackButton(language));

		if (nextButton)
			rowsInLine.add(createNextButton(language));

		inlineKeyboardMarkup.setKeyboard(rowsInLine);
		return inlineKeyboardMarkup;
	}

	// Common method to create list
	protected List<String> createList(String base) {
		List<String> list = new ArrayList<>();
		String code;
		boolean end = false;
		int i = 1;
		while (!end) {
			code = base + "." + i;
			if (getButtonTextByCode(code, defaultLanguage.toString()).equals("error")) {
				end = true;
			} else {
				list.add(code);
				i++;
			}
		}

		return list;
	}

}