package ua.gexlq.TelegramStudyBot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.gexlq.TelegramStudyBot.entity.DownloadedFile;


public interface DownloadedFileDAO extends JpaRepository<DownloadedFile, Long> {

	public DownloadedFile findByFilePath(String filePath);
	
	public DownloadedFile findDownloadedFileById(Long id);
	
	 @Query(value = "SELECT * FROM downloaded_files ORDER BY id DESC LIMIT 1", nativeQuery = true)
	 public DownloadedFile findLastDownloadedFile();
}