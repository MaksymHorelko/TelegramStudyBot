package ua.gexlq.TelegramStudyBot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import ua.gexlq.TelegramStudyBot.handler.MessageHandler;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

	// BACK BUTTON
	public static KeyboardRow createBackMenuButton(String language) {
		KeyboardRow backButton = new KeyboardRow();
		backButton.add(MessageHandler.getMessage("menu.back", language));

		return backButton;
	}
	
	// MAIN MENU
	public static ReplyKeyboardMarkup createMainMenuKeyboard(String language) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(MessageHandler.getMessage("menu.works", language));

		KeyboardRow row2 = new KeyboardRow();
		row2.add(MessageHandler.getMessage("menu.materials", language));
		row2.add(MessageHandler.getMessage("menu.help", language));

		KeyboardRow row3 = new KeyboardRow();
		row3.add(MessageHandler.getMessage("menu.settings", language));

		keyboard.add(row1);
		keyboard.add(row2);
		keyboard.add(row3);

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

	// WORK MENU
	public static ReplyKeyboardMarkup createWorkMenuKeyboard(String language) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(MessageHandler.getMessage("menu.works.subject", language));
		row1.add(MessageHandler.getMessage("menu.works.view", language));
		
		keyboard.add(row1);
		
		keyboard.add(createBackMenuButton(language));

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

	// HELP MENU
	public static ReplyKeyboardMarkup createHelpMenuKeyboard(String language) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(MessageHandler.getMessage("menu.help.commands", language));
		row1.add(MessageHandler.getMessage("menu.help.donate", language));

		KeyboardRow row2 = new KeyboardRow();
		row2.add(MessageHandler.getMessage("menu.help.email", language));
		row2.add(MessageHandler.getMessage("menu.help.chat", language));

		keyboard.add(row1);
		keyboard.add(row2);
		keyboard.add(createBackMenuButton(language));

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

	// SETTINGS MENU
	public static ReplyKeyboardMarkup createSettingsMenuKeyboard(boolean isUserRegister, String language) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(MessageHandler.getMessage("menu.settings.language", language));
		row1.add(MessageHandler.getMessage("menu.settings.notifications", language));
		
		if(isUserRegister)
			row1.add(MessageHandler.getMessage("menu.settings.data", language));

		keyboard.add(row1);
		keyboard.add(createBackMenuButton(language));

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

	// MATERIALS MENU
	public static ReplyKeyboardMarkup createMaterialsMenuKeyboard(String language) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(MessageHandler.getMessage("menu.materials.lectures", language));
		row1.add(MessageHandler.getMessage("menu.materials.semester", language));
		row1.add(MessageHandler.getMessage("menu.materials.literature", language));

		keyboard.add(row1);
		keyboard.add(createBackMenuButton(language));

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}
	
	// MATERIALS MENU
	public static ReplyKeyboardMarkup createPickFacultyMenuKeyboard(String language) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(MessageHandler.getMessage("menu.materials.lectures", language));
		row1.add(MessageHandler.getMessage("menu.materials.course", language));
		row1.add(MessageHandler.getMessage("menu.materials.literature", language));

		keyboard.add(row1);
		keyboard.add(createBackMenuButton(language));

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

}