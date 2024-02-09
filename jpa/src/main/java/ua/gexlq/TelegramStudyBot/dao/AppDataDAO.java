package ua.gexlq.TelegramStudyBot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.gexlq.TelegramStudyBot.entity.AppData;

public interface AppDataDAO extends JpaRepository<AppData, Long> {
	
	 @Query(value = "SELECT * FROM app_data ORDER BY id DESC LIMIT 1", nativeQuery = true)
	 public AppData getAppData();
}
