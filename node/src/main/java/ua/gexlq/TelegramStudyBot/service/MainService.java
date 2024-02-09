package ua.gexlq.TelegramStudyBot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MainService {
	public void processTextMessage(Update update);
	
	public void processDocMessage(Update update);
	
	public void processCallBackDataMessage(Update update);
	
}
