package ua.gexlq.TelegramStudyBot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {
	public void consumeTextMessageUpdate(Update update);

	public void consumeDocMessageUpdate(Update update);

	public void consumeCallBackDataMessageUpdate(Update update);

}
