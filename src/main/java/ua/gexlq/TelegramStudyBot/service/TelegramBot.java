package ua.gexlq.TelegramStudyBot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.slf4j.Slf4j;
import ua.gexlq.TelegramStudyBot.config.Config;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

	final Config config;

	public TelegramBot(Config config) {
		this.config = config;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			String message = update.getMessage().getText();

			long chatId = update.getMessage().getChatId();

			switch (message) {
			case "/start": {
				startCommandReceived(chatId, update.getMessage().getChat().getUserName());
				break;
			}

			default:
				sendMessage(chatId, "default message");
			}
		}
	}

	private void startCommandReceived(long chatId, String name) {
		String answer = name + ", hi!";
		sendMessage(chatId, answer);
	}

	private void sendMessage(long chatId, String textToSend) {
		SendMessage message = new SendMessage();

		message.setChatId(String.valueOf(chatId));

		message.setText(textToSend);

		try {
			execute(message);
		} catch (TelegramApiException e) {
			log.error("Error occurred: " + e.getMessage());
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
