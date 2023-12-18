package ua.gexlq.TelegramStudyBot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import lombok.extern.slf4j.Slf4j;
import ua.gexlq.TelegramStudyBot.service.TelegramBot;

@Slf4j
@Component
public class Initializer {

	@Autowired
	TelegramBot bot;

	@EventListener({ ContextRefreshedEvent.class })
	public void init() throws TelegramApiException {
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		try {
			telegramBotsApi.registerBot(bot);
		} catch (Exception e) {
			log.error("Error occurred: " + e.getMessage());
		}
	}

}
