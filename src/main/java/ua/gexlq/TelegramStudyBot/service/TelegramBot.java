package ua.gexlq.TelegramStudyBot.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ua.gexlq.TelegramStudyBot.config.Config;
import ua.gexlq.TelegramStudyBot.handler.MenuHandler;
import ua.gexlq.TelegramStudyBot.handler.Logger;
import ua.gexlq.TelegramStudyBot.model.Repository;
import ua.gexlq.TelegramStudyBot.model.UserManager;

@Component
public class TelegramBot extends TelegramLongPollingBot {

	
	@Autowired
	private Repository repository;
	
	@Autowired
	private UserManager userManager;
	
	
	final Config config;

	public TelegramBot(Config config) {
		this.config = config;


	}

	@Override
	public void onUpdateReceived(Update update) {
		
		if (update.hasMessage() && update.getMessage().hasText()) {
			
			long chatId = update.getMessage().getChatId();
			String locale = userManager.getUserLanguage(chatId);
			
			
			switch (MenuHandler.getBotState()) {
			case MAIN_MENU:
				
				if(update.getMessage().getText().equals("/start")) {
					firstLaunch(update);
				}
				
				send(MenuHandler.mainMenu(update, locale));
				break;

			case WORK_MENU:
				send(MenuHandler.workMenu(update, locale));
				break;

			case HELP_MENU:
				send(MenuHandler.helpMenu(update, locale));
				break;

			case SETTINGS_MENU:
				send(MenuHandler.settingsMenu(update, locale));
				break;
				
			case MATERIALS_MENU:
				send(MenuHandler.materialsMenu(update, locale));
				break;
			}
		}
	}

	private void firstLaunch(Update update) {
		userManager.registerUser(update);
	}
	
	private void send(SendMessage message) {
		try {
			execute(message);
		} catch (TelegramApiException e) {
			Logger.logError(e);
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
