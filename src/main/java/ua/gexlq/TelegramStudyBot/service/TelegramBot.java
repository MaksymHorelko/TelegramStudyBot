package ua.gexlq.TelegramStudyBot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.slf4j.Slf4j;
import ua.gexlq.TelegramStudyBot.config.Config;
import ua.gexlq.TelegramStudyBot.handler.MessageHandler;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

	private String locale = "ua";

	final Config config;

	public TelegramBot(Config config) {
		this.config = config;

	}

	private enum BotState {
		MAIN_MENU, WORK_MENU, HELP_MENU, SETTINGS_MENU
	}

	private BotState botState = BotState.MAIN_MENU;

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			long chatId = update.getMessage().getChatId();
			String input = update.getMessage().getText();

			switch (botState) {
			case MAIN_MENU:
				handleMainMenu(chatId, input);
				break;

			case WORK_MENU:
				handleWorkMenu(chatId, input);
				break;

			case HELP_MENU:
				handleHelpMenu(chatId, input);
				break;

			case SETTINGS_MENU:
				handleSettingsMenu(chatId, input);
				break;
			}
		}
	}

	private void handleMainMenu(long chatId, String input) {
		SendMessage message = new SendMessage();
		message.setChatId(String.valueOf(chatId));

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

		send(message);
	}

	private void handleHelpMenu(long chatId, String input) {
		SendMessage message = new SendMessage();
		message.setChatId(String.valueOf(chatId));

		if (input.equals(MessageHandler.getMessage("menu.help.email", locale))) {
			message.setText(MessageHandler.getMessage("message.help.email", locale).replace("{0}",
					MessageHandler.getMessage("email", locale)));
		}

		else if (input.equals(MessageHandler.getMessage("menu.help.chat", locale))) {
			message.setText(MessageHandler.getMessage("message.help.chat", locale).replace("{0}",
					MessageHandler.getMessage("chatURL", locale)));
		}

		else if (input.equals(MessageHandler.getMessage("menu.help.tech", locale))) {
			message.setText(MessageHandler.getMessage("message.help.tech", locale));
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

		send(message);
	}

	private void handleWorkMenu(long chatId, String input) {
		SendMessage message = new SendMessage();
		message.setChatId(String.valueOf(chatId));

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

		send(message);
	}

	private void handleSettingsMenu(long chatId, String input) {
		SendMessage message = new SendMessage();
		message.setChatId(String.valueOf(chatId));

		if (input.equals(MessageHandler.getMessage("menu.settings.language", locale))) {
			message.setText(MessageHandler.getMessage("message.settings.language", locale));
			
		}

		else if (input.equals(MessageHandler.getMessage("menu.settings.notifications", locale))) {
			message.setText(MessageHandler.getMessage("message.settings.notifications", locale));
		}

		else if (input.equals(MessageHandler.getMessage("menu.settings.data", locale))) {
			message.setText(MessageHandler.getMessage("message.settings.data", locale));
		}
		
		else if(input.equals(MessageHandler.getMessage("menu.back", locale))) {
			message.setText(MessageHandler.getMessage("message.stepBackToMainMenu", locale));
			message.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(locale));
			botState = BotState.MAIN_MENU;
		}
		
		else {
			message.setText(MessageHandler.getMessage("message.unknownCommand", locale));
			message.setReplyMarkup(KeyboardFactory.createSettingsMenuKeyboard(locale));
		}
		
		send(message);
	}

	private void send(SendMessage message) {
		try {
			execute(message);
		} catch (TelegramApiException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public String getBotUsername() {
		return config.getBotName();
	}

	@Override
	public String getBotToken() {
		return config.getBotToken();
	}

}
