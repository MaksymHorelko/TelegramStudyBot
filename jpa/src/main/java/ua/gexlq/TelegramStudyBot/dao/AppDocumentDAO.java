package ua.gexlq.TelegramStudyBot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.gexlq.TelegramStudyBot.entity.AppDocument;

public interface AppDocumentDAO extends JpaRepository<AppDocument, Long> {

	 @Query(value = "SELECT * FROM app_document ORDER BY id DESC LIMIT 1", nativeQuery = true)
	 public AppDocument findLatestDocument();
}
