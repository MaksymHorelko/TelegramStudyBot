package ua.gexlq.TelegramStudyBot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.gexlq.TelegramStudyBot.entity.AppSoftware;

public interface AppSoftwareDAO extends JpaRepository<AppSoftware, Long> {
	@Query(value = "SELECT * FROM app_software ORDER BY id DESC LIMIT 1", nativeQuery = true)
	public AppSoftware findLastSoftware();

	public AppSoftware findBySoftwareName(String softwareName);

}
