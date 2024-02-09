package ua.gexlq.TelegramStudyBot.service;

import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import ua.gexlq.TelegramStudyBot.entity.DownloadedFile;

public interface ProducerService {
	public void produceAnswer(SendMessage sendMessage);
	
	public void produceAnswer(EditMessageText sendMessage);
	
	public void produceAnswer(ForwardMessage sendMessage);
	
	public void produceAnswer(DownloadedFile file);
}
