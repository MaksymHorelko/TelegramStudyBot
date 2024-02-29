package ua.gexlq.TelegramStudyBot.keyboard.menu.menus;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import ua.gexlq.TelegramStudyBot.keyboard.menu.KeyboardFactory;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@Component
public class SettingsMenu extends KeyboardFactory {

	public SettingsMenu(MessageUtils messageUtils) {
		super(messageUtils);
	}

	// MAIN MENU -> SETTINGS MENU
	public ReplyKeyboardMarkup createSettingsMenuKeyboard(boolean isUserRegistered, String language) {
		List<String> buttons = new ArrayList<String>();
		
		buttons.add("menu.settings.language");
		buttons.add("menu.settings.notifications");
		
		if (isUserRegistered)
			buttons.add("menu.settings.data");

		List<KeyboardRow> keyboard = new ArrayList<>();
		keyboard.add(createKeyboardRow(buttons, language));

		keyboard.add(createBackMenuButton(language));

		return createReplyKeyboardMarkup(keyboard);
	}

}
