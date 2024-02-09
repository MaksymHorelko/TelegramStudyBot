package ua.gexlq.TelegramStudyBot.service;

import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import ua.gexlq.TelegramStudyBot.entity.DownloadedFile;

public interface AnswerConsumer {
	public void consume(SendMessage sendMessage);
	
	public void consume(EditMessageText sendMessage);
	
	public void consume(ForwardMessage sendMessage);
	
	public void consume(DownloadedFile file);
	
}
