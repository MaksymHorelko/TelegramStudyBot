package ua.gexlq.TelegramStudyBot.service;

import org.springframework.http.ResponseEntity;

public interface FileInfoService {
	public ResponseEntity<String> getFilePath(String fileId);
}
