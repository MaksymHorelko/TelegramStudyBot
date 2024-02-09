package ua.gexlq.TelegramStudyBot.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.entity.DownloadedFile;
import ua.gexlq.TelegramStudyBot.service.ProducerService;

import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.ANSWER_TEXT_MESSAGE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.ANSWER_EDIT_MESSAGE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.ANSWER_FORWARD_MESSAGE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.ANSWER_DOCUMENT_MESSAGE;

@RequiredArgsConstructor
@Service
public class ProducerServiceImpl implements ProducerService {
	private final RabbitTemplate rabbitTemplate;

	@Override
	public void produceAnswer(SendMessage sendMessage) {
		rabbitTemplate.convertAndSend(ANSWER_TEXT_MESSAGE, sendMessage);
	}

	@Override
	public void produceAnswer(EditMessageText sendMessage) {
		rabbitTemplate.convertAndSend(ANSWER_EDIT_MESSAGE, sendMessage);
	}

	@Override
	public void produceAnswer(ForwardMessage sendMessage) {
		rabbitTemplate.convertAndSend(ANSWER_FORWARD_MESSAGE, sendMessage);
	}

	@Override
	public void produceAnswer(DownloadedFile file) {
		rabbitTemplate.convertAndSend(ANSWER_DOCUMENT_MESSAGE, file);

	}
}
