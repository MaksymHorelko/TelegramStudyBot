package ua.gexlq.TelegramStudyBot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import ua.gexlq.TelegramStudyBot.handler.MessageHandler;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

	public static ReplyKeyboardMarkup createMainMenuKeyboard(String locale) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(MessageHandler.getMessage("menu.works", locale));

		KeyboardRow row2 = new KeyboardRow();
		row2.add(MessageHandler.getMessage("menu.materials", locale));
		row2.add(MessageHandler.getMessage("menu.help", locale));

		KeyboardRow row3 = new KeyboardRow();
		row3.add(MessageHandler.getMessage("menu.settings", locale));

		keyboard.add(row1);
		keyboard.add(row2);
		keyboard.add(row3);

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

	public static ReplyKeyboardMarkup createWorkMenuKeyboard(String locale) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(MessageHandler.getMessage("menu.works.subject", locale));

		KeyboardRow row2 = new KeyboardRow();
		row2.add(MessageHandler.getMessage("menu.works.upload", locale));
		row2.add(MessageHandler.getMessage("menu.works.view", locale));

		KeyboardRow row3 = new KeyboardRow();

		row3.add(MessageHandler.getMessage("menu.back", locale));

		keyboard.add(row1);
		keyboard.add(row2);
		keyboard.add(row3);

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

	public static ReplyKeyboardMarkup createHelpMenuKeyboard(String locale) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(MessageHandler.getMessage("menu.help.commands", locale));
		row1.add(MessageHandler.getMessage("menu.help.tech", locale));

		KeyboardRow row2 = new KeyboardRow();
		row2.add(MessageHandler.getMessage("menu.help.email", locale));
		row2.add(MessageHandler.getMessage("menu.help.chat", locale));

		KeyboardRow row3 = new KeyboardRow();
		row3.add(MessageHandler.getMessage("menu.back", locale));

		keyboard.add(row1);
		keyboard.add(row2);
		keyboard.add(row3);

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

	public static ReplyKeyboardMarkup createBackMenuKeyboard(String locale) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(MessageHandler.getMessage("menu.back", locale));
		keyboard.add(row1);

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}
	
	
	public static ReplyKeyboardMarkup createSettingsMenuKeyboard(String locale) {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		keyboardMarkup.setSelective(true);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow row1 = new KeyboardRow();
		row1.add(MessageHandler.getMessage("menu.settings.language", locale));
		row1.add(MessageHandler.getMessage("menu.settings.notifications", locale));
		row1.add(MessageHandler.getMessage("menu.settings.data", locale));
		
		KeyboardRow row2 = new KeyboardRow();
		row2.add(MessageHandler.getMessage("menu.back", locale));
		
		
		keyboard.add(row1);
		keyboard.add(row2);

		keyboardMarkup.setKeyboard(keyboard);

		return keyboardMarkup;
	}

}