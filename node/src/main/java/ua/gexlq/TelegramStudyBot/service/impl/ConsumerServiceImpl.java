package ua.gexlq.TelegramStudyBot.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.service.ConsumerService;
import ua.gexlq.TelegramStudyBot.service.MainService;

import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.TEXT_MESSAGE_UPDATE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.DOC_MESSAGE_UPDATE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.CALLBACKQUERY_MESSAGE_UPDATE;

@RequiredArgsConstructor
@Service
public class ConsumerServiceImpl implements ConsumerService {

	private final MainService mainService;

	@Override
	@RabbitListener(queues = TEXT_MESSAGE_UPDATE)
	public void consumeTextMessageUpdate(Update update) {
		mainService.processTextMessage(update);
	}

	@Override
	@RabbitListener(queues = DOC_MESSAGE_UPDATE)
	public void consumeDocMessageUpdate(Update update) {
		mainService.processDocMessage(update);
	}

	@Override
	@RabbitListener(queues = CALLBACKQUERY_MESSAGE_UPDATE)
	public void consumeCallBackDataMessageUpdate(Update update) {
		mainService.processCallBackDataMessage(update);
	}

}
