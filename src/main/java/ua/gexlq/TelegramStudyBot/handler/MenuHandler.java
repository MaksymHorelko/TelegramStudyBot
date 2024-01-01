package ua.gexlq.TelegramStudyBot.handler;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import ua.gexlq.TelegramStudyBot.service.KeyboardFactory;

public class MenuHandler {

	public static enum BotState {
		MAIN_MENU, WORK_MENU, HELP_MENU, SETTINGS_MENU, MATERIALS_MENU;
	}

	private static BotState botState = BotState.MAIN_MENU;

	public static BotState getBotState() {
		return botState;
	}

	private static long getUserId(Update update) {
		return update.getMessage().getChatId();
	}

	private static String getUserMessage(Update update) {
		return update.getMessage().getText();
	}

	private static SendMessage setMessage(long chatId) {
		SendMessage message = new SendMessage();
		message.setChatId(String.valueOf(chatId));
		return message;
	}

	public static SendMessage mainMenu(Update update, String locale) {

		long chatId = getUserId(update);
		String input = getUserMessage(update);

		SendMessage message = setMessage(chatId);

		if (input.equals("/start")) {
			message.setText(MessageHandler.getMessage("message.welcome", locale));
			message.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(locale));
		}

		else if (input.equals(MessageHandler.getMessage("menu.works", locale))) {
			message.setText(MessageHandler.getMessage("message.works", locale));
			message.setReplyMarkup(KeyboardFactory.createWorkMenuKeyboard(locale));
			botState = BotState.WORK_MENU;
		}

		else if (input.equals(MessageHandler.getMessage("menu.materials", locale))) {
			message.setText(MessageHandler.getMessage("message.materials", locale));
			message.setReplyMarkup(KeyboardFactory.createMaterialsMenuKeyboard(locale));
			botState = BotState.MATERIALS_MENU;
		}

		else if (input.equals(MessageHandler.getMessage("menu.help", locale))) {
			message.setText(MessageHandler.getMessage("message.help", locale));
			message.setReplyMarkup(KeyboardFactory.createHelpMenuKeyboard(locale));
			botState = BotState.HELP_MENU;
		}

		else if (input.equals(MessageHandler.getMessage("menu.settings", locale))) {
			message.setText(MessageHandler.getMessage("message.settings", locale));
			message.setReplyMarkup(KeyboardFactory.createSettingsMenuKeyboard(locale));
			botState = BotState.SETTINGS_MENU;
		}

		else {
			message.setText(MessageHandler.getMessage("message.unknownCommand", locale));
			message.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(locale));
		}

		return message;
	}

	public static SendMessage helpMenu(Update update, String locale) {
		long chatId = getUserId(update);
		String input = getUserMessage(update);

		SendMessage message = setMessage(chatId);

		if (input.equals(MessageHandler.getMessage("menu.help.email", locale))) {
			message.setText(MessageHandler.getMessage("message.help.email", locale).replace("{0}",
					MessageHandler.getMessage("email", locale)));
		}

		else if (input.equals(MessageHandler.getMessage("menu.help.chat", locale))) {
			message.setText(MessageHandler.getMessage("message.help.chat", locale).replace("{0}",
					MessageHandler.getMessage("chatURL", locale)));
		}

		else if (input.equals(MessageHandler.getMessage("menu.help.donate", locale))) {
			message.setText(MessageHandler.getMessage("message.help.donate", locale));
		}

		else if (input.equals(MessageHandler.getMessage("menu.help.commands", locale))) {
			message.setText(MessageHandler.getMessage("message.help.commands", locale));
		}

		else if (input.equals(MessageHandler.getMessage("menu.back", locale))) {
			message.setText(MessageHandler.getMessage("message.stepBackToMainMenu", locale));
			message.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(locale));
			botState = BotState.MAIN_MENU;
		}

		else {
			message.setText(MessageHandler.getMessage("message.unknownCommand", locale));
			message.setReplyMarkup(KeyboardFactory.createHelpMenuKeyboard(locale));
		}
		
		return message;
	}

	public static SendMessage workMenu(Update update, String locale) {
		long chatId = getUserId(update);
		String input = getUserMessage(update);

		SendMessage message = setMessage(chatId);

		if (input.equals(MessageHandler.getMessage("menu.works.subject", locale))) {
			message.setText(MessageHandler.getMessage("message.works.subject", locale));
		}

		else if (input.equals(MessageHandler.getMessage("menu.works.upload", locale))) {
			message.setText(MessageHandler.getMessage("message.works.upload", locale));
		}

		else if (input.equals(MessageHandler.getMessage("menu.works.view", locale))) {
			message.setText(MessageHandler.getMessage("message.works.view", locale));
		}

		else if (input.equals(MessageHandler.getMessage("menu.back", locale))) {
			message.setText(MessageHandler.getMessage("message.stepBackToMainMenu", locale));
			message.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(locale));
			botState = BotState.MAIN_MENU;
		}

		else {
			message.setText(MessageHandler.getMessage("message.unknownCommand", locale));
			message.setReplyMarkup(KeyboardFactory.createWorkMenuKeyboard(locale));
		}
		
		return message;
	}

	public static SendMessage settingsMenu(Update update, String locale) {
		long chatId = getUserId(update);
		String input = getUserMessage(update);

		SendMessage message = setMessage(chatId);

		if (input.equals(MessageHandler.getMessage("menu.settings.language", locale))) {
			message.setText(MessageHandler.getMessage("message.settings.language", locale));

		}

		else if (input.equals(MessageHandler.getMessage("menu.settings.notifications", locale))) {
			message.setText(MessageHandler.getMessage("message.settings.notifications", locale));
		}

		else if (input.equals(MessageHandler.getMessage("menu.settings.data", locale))) {
			message.setText(MessageHandler.getMessage("message.settings.data", locale));
		}

		else if (input.equals(MessageHandler.getMessage("menu.back", locale))) {
			message.setText(MessageHandler.getMessage("message.stepBackToMainMenu", locale));
			message.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(locale));
			botState = BotState.MAIN_MENU;
		}

		else {
			message.setText(MessageHandler.getMessage("message.unknownCommand", locale));
			message.setReplyMarkup(KeyboardFactory.createSettingsMenuKeyboard(locale));
		}

		return message;
	}

	public static SendMessage materialsMenu(Update update, String locale) {
		long chatId = getUserId(update);
		String input = getUserMessage(update);

		SendMessage message = setMessage(chatId);

		if (input.equals(MessageHandler.getMessage("menu.materials.lectures", locale))) {
			message.setText(MessageHandler.getMessage("message.materials.lectures", locale));

		}

		else if (input.equals(MessageHandler.getMessage("menu.materials.course", locale))) {
			message.setText(MessageHandler.getMessage("message.materials.course", locale));

		}

		else if (input.equals(MessageHandler.getMessage("menu.materials.literature", locale))) {
			message.setText(MessageHandler.getMessage("message.materials.literature", locale));

		}

		else if (input.equals(MessageHandler.getMessage("menu.back", locale))) {
			message.setText(MessageHandler.getMessage("message.stepBackToMainMenu", locale));
			message.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(locale));
			botState = BotState.MAIN_MENU;
		}

		else {
			message.setText(MessageHandler.getMessage("message.unknownCommand", locale));
			message.setReplyMarkup(KeyboardFactory.createMaterialsMenuKeyboard(locale));
		}
		
		return message;
	}

}
