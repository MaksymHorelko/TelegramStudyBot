package ua.gexlq.TelegramStudyBot.service;


import org.telegram.telegrambots.meta.api.objects.Update;

import ua.gexlq.TelegramStudyBot.entity.DownloadedFile;

public interface FileDownloader {
	public DownloadedFile downloadDocument(Update update);
}
