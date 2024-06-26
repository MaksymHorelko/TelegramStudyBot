package ua.gexlq.TelegramStudyBot.cotroller;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.gexlq.TelegramStudyBot.entity.DownloadedFile;
import ua.gexlq.TelegramStudyBot.service.UpdateProducer;
import ua.gexlq.TelegramStudyBot.service.impl.DocumentService;

import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.TEXT_MESSAGE_UPDATE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.DOC_MESSAGE_UPDATE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.CALLBACKQUERY_MESSAGE_UPDATE;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdateController {

	private final UpdateProducer updateProducer;
	private final DocumentService documentService;
	private TelegramBot telegramBot;

	public void registerBot(TelegramBot telegramBot) {
		this.telegramBot = telegramBot;
	}

	public void processUpdate(Update update) {
		if (update == null) {
			log.error("Received update is null");
			return;
		}

		if (update.hasMessage() || update.hasCallbackQuery())
			distributeMessageByType(update);

		else
			log.error("Received unsupported message type : " + update);

	}

	private void distributeMessageByType(Update update) {
		var message = update.getMessage();

		if (update.hasCallbackQuery())
			processCallbackQuery(update);

		else if (message.hasText())
			processTextMessage(update);

		else if (message.hasDocument())
			processDocumentMessage(update);

	}

	public void setView(SendMessage sendMessage) {
		telegramBot.sendAnswer(sendMessage);
	}

	public void setView(EditMessageText sendMessage) {
		telegramBot.sendAnswer(sendMessage);
	}

	public void setView(ForwardMessage sendMessage) {
		telegramBot.sendAnswer(sendMessage);
	}

	public void setView(DownloadedFile file) {
		telegramBot.sendAnswer(documentService.createSendDocument(file));
	}

	private void processTextMessage(Update update) {
		updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
	}

	private void processDocumentMessage(Update update) {
		updateProducer.produce(DOC_MESSAGE_UPDATE, update);
	}

	private void processCallbackQuery(Update update) {
		updateProducer.produce(CALLBACKQUERY_MESSAGE_UPDATE, update);
	}

}
