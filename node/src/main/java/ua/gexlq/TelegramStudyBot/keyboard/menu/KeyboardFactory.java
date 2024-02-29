package ua.gexlq.TelegramStudyBot.keyboard.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

import java.util.List;

@RequiredArgsConstructor
@Component
public abstract class KeyboardFactory {

	private final MessageUtils messageUtils;

	private static final String MENU_BACK = "menu.back";

	// Common method to create KeyboardRow
	protected KeyboardRow createKeyboardRow(List <String> buttonText, String language) {
		KeyboardRow row = new KeyboardRow();
		for(String text : buttonText)
			row.add(messageUtils.getAnswerTextByCode(text, language));
		
		return row;
	}

	// Back Button
	protected KeyboardRow createBackMenuButton(String language) {
		return createKeyboardRow(List.of(MENU_BACK), language);
	}

	// Common method to create ReplyKeyboardMarkup
	protected ReplyKeyboardMarkup createReplyKeyboardMarkup(List<KeyboardRow> keyboard) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);
		keyboardMarkup.setKeyboard(keyboard);
		return keyboardMarkup;
	}

}