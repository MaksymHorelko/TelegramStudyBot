package ua.gexlq.TelegramStudyBot.cotroller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.annotation.PostConstruct;

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
import ua.gexlq.TelegramStudyBot.dao.DownloadedFileDAO;
import ua.gexlq.TelegramStudyBot.entity.DownloadedFile;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {

	private final UpdateController updateController;

	private final DownloadedFileDAO downloadedFileDAO;

	private final Config config;

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

	public void sendAnswer(SendDocument sendDocument, DownloadedFile file) {
		try {

			long nextTempId;
			var lastDownloadedFile = downloadedFileDAO.findLastDownloadedFile();
			if (lastDownloadedFile == null) {
				var scan = new Scanner(System.in);
				System.out.println("Enter LAST_MESSAGE_ID in your 'temp folder' CHAT: ");
				nextTempId = scan.nextLong() + 1;
				scan.close();

			} else {
				nextTempId = lastDownloadedFile.getMessageIdInTemp() + 1;
			}

			file.setMessageIdInTemp(nextTempId);
			downloadedFileDAO.save(file);

			execute(sendDocument);

			Path path = Paths.get(file.getFilePath());
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