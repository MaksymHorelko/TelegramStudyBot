package ua.gexlq.TelegramStudyBot.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.gexlq.TelegramStudyBot.entity.DownloadedFile;

public interface DownloadedFileDAO extends JpaRepository<DownloadedFile, Long> {

	public DownloadedFile findByFilePath(String filePath);
}