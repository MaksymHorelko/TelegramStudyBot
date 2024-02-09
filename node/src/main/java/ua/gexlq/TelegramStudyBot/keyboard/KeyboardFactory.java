package ua.gexlq.TelegramStudyBot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class KeyboardFactory {

	private final MessageUtils messageUtils;

	// BACK BUTTON
	public KeyboardRow createBackMenuButton(String language) {
		KeyboardRow backButton = new KeyboardRow();
		backButton.add(messageUtils.getAnswerTextByCode("menu.back", language));

		return backButton;
	}

	// MAIN MENU
	public ReplyKeyboardMarkup createMainMenuKeyboard(String language) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(messageUtils.getAnswerTextByCode("menu.works.name", language));

		KeyboardRow row2 = new KeyboardRow();
		row2.add(messageUtils.getAnswerTextByCode("menu.materials.name", language));
		row2.add(messageUtils.getAnswerTextByCode("menu.help.name", language));

		KeyboardRow row3 = new KeyboardRow();
		row3.add(messageUtils.getAnswerTextByCode("menu.settings.name", language));

		keyboard.add(row1);
		keyboard.add(row2);
		keyboard.add(row3);

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

	// WORK MENU
	public ReplyKeyboardMarkup createWorksMenuKeyboard(String language) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(messageUtils.getAnswerTextByCode("menu.works.works", language));
		row1.add(messageUtils.getAnswerTextByCode("menu.works.view", language));

		keyboard.add(row1);

		keyboard.add(createBackMenuButton(language));

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

	// HELP MENU
	public ReplyKeyboardMarkup createHelpMenuKeyboard(String language) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(messageUtils.getAnswerTextByCode("menu.help.commands", language));
		row1.add(messageUtils.getAnswerTextByCode("menu.help.donate", language));

		KeyboardRow row2 = new KeyboardRow();
		row2.add(messageUtils.getAnswerTextByCode("menu.help.contact", language));
		row2.add(messageUtils.getAnswerTextByCode("menu.help.chat", language));

		keyboard.add(row1);
		keyboard.add(row2);
		keyboard.add(createBackMenuButton(language));

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

	// SETTINGS MENU
	public ReplyKeyboardMarkup createSettingsMenuKeyboard(boolean isUserRegister, String language) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(messageUtils.getAnswerTextByCode("menu.settings.language", language));
		row1.add(messageUtils.getAnswerTextByCode("menu.settings.notifications", language));

		if (isUserRegister)
			row1.add(messageUtils.getAnswerTextByCode("menu.settings.data", language));

		keyboard.add(row1);
		keyboard.add(createBackMenuButton(language));

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

	// MATERIALS MENU
	public ReplyKeyboardMarkup createMaterialsMenuKeyboard(String language) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(messageUtils.getAnswerTextByCode("menu.materials.lectures", language));
		row1.add(messageUtils.getAnswerTextByCode("menu.materials.semester", language));
		row1.add(messageUtils.getAnswerTextByCode("menu.materials.literature", language));

		keyboard.add(row1);
		keyboard.add(createBackMenuButton(language));

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

}