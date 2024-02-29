package ua.gexlq.TelegramStudyBot.keyboard.menu.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import ua.gexlq.TelegramStudyBot.keyboard.menu.KeyboardFactory;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@Component
public class MainMenu extends KeyboardFactory {

	public MainMenu(MessageUtils messageUtils) {
		super(messageUtils);
	}

	// MAIN MENU
	public ReplyKeyboardMarkup createMainMenuKeyboard(String language) {
		List<String> firstButtonsRow = Arrays.asList("menu.works.name");
		List<String> secondButtonsRow = Arrays.asList("menu.software.name", "menu.help.name");
		List<String> thirdButtonsRow = Arrays.asList("menu.settings.name");

		List<KeyboardRow> keyboard = new ArrayList<>();

		keyboard.add(createKeyboardRow(firstButtonsRow, language));
		keyboard.add(createKeyboardRow(secondButtonsRow, language));
		keyboard.add(createKeyboardRow(thirdButtonsRow, language));

		return createReplyKeyboardMarkup(keyboard);
	}
}
