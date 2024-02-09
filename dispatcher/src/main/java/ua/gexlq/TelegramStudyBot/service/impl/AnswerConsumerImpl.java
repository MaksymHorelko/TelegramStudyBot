package ua.gexlq.TelegramStudyBot.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.cotroller.UpdateController;
import ua.gexlq.TelegramStudyBot.entity.DownloadedFile;
import ua.gexlq.TelegramStudyBot.service.AnswerConsumer;

import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.ANSWER_TEXT_MESSAGE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.ANSWER_EDIT_MESSAGE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.ANSWER_FORWARD_MESSAGE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.ANSWER_DOCUMENT_MESSAGE;;

@RequiredArgsConstructor
@Service
public class AnswerConsumerImpl implements AnswerConsumer {

	private final UpdateController updateController;

	@Override
	@RabbitListener(queues = ANSWER_TEXT_MESSAGE)
	public void consume(SendMessage sendMessage) {
		updateController.setView(sendMessage);
	}

	@Override
	@RabbitListener(queues = ANSWER_EDIT_MESSAGE)
	public void consume(EditMessageText sendMessage) {
		updateController.setView(sendMessage);
	}

	@Override
	@RabbitListener(queues = ANSWER_FORWARD_MESSAGE)
	public void consume(ForwardMessage sendMessage) {
		updateController.setView(sendMessage);
	}

	@Override
	@RabbitListener(queues = ANSWER_DOCUMENT_MESSAGE)
	public void consume(DownloadedFile file) {
		updateController.setView(file);
	}
}
