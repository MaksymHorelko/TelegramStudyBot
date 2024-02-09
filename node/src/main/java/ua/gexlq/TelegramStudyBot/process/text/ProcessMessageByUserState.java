package ua.gexlq.TelegramStudyBot.process.text;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import ua.gexlq.TelegramStudyBot.entity.enums.UserState;

public interface ProcessMessageByUserState {
	public SendMessage handle(Update update);

	public UserState getSupportedState();

}
