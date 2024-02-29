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
public class HelpMenu extends KeyboardFactory {

	public HelpMenu(MessageUtils messageUtils) {
		super(messageUtils);
	}

	// MAIN MENU -> HELP MENU
	public ReplyKeyboardMarkup createHelpMenuKeyboard(String language) {
		List<String> firstButtonsRow = Arrays.asList("menu.help.rules", "menu.help.disclaimer");
		List<String> secondButtonsRow = Arrays.asList("menu.help.github", "menu.help.donate");
		List<String> thirdButtonsRow = Arrays.asList("menu.help.contact", "menu.help.chat");
		
		List<KeyboardRow> keyboard = new ArrayList<>();
		
		keyboard.add(createKeyboardRow(firstButtonsRow, language));
		keyboard.add(createKeyboardRow(secondButtonsRow, language));
		keyboard.add(createKeyboardRow(thirdButtonsRow, language));
		keyboard.add(createBackMenuButton(language));

		return createReplyKeyboardMarkup(keyboard);
	}

}
