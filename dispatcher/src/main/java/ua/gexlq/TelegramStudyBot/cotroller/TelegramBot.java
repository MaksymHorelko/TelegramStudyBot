package ua.gexlq.TelegramStudyBot.cotroller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.gexlq.TelegramStudyBot.config.Config;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {

	private final UpdateController updateController;

	private final Config config;

	@Value("${file.path}")
	private String folderPath;

	@PostConstruct
	public void init() {
		updateController.registerBot(this);
	}

	@Override
	public void onUpdateReceived(Update update) {
		updateController.processUpdate(update);
	}

	public void sendAnswer(SendMessage message) {
		try {
			execute(message);
		} catch (TelegramApiException e) {
			log.error(e.getMessage());
		}
	}

	public void sendAnswer(ForwardMessage forwardMessage) {
		try {
			execute(forwardMessage);
		} catch (TelegramApiException e) {
			log.error(e.getMessage());
		}
	}

	public void sendAnswer(EditMessageText editMessageText) {
		try {
			execute(editMessageText);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public void sendAnswer(SendDocument sendDocument) {
		try {
			execute(sendDocument);
			
			String attachFileName = sendDocument.getDocument().getAttachName();
			String filePath = folderPath + attachFileName.substring(attachFileName.lastIndexOf("/") + 1);

			Path path = Paths.get(filePath);
			Files.delete(path);
		} catch (Exception e) {
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