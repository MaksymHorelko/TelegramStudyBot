package ua.gexlq.TelegramStudyBot.utils;

import java.util.List;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.gexlq.TelegramStudyBot.dao.AppDataDAO;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.entity.AppUser;
import ua.gexlq.TelegramStudyBot.entity.enums.FileState;
import ua.gexlq.TelegramStudyBot.exceptions.FileCheckException;
import ua.gexlq.TelegramStudyBot.service.ProducerService;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Component
public class Scheduler {
	private final FileCheckSender fileCheckSender;
	private final AppDataDAO appDataDAO;
	private final AppUserDAO appUserDAO;
	private final UserPermissionsService permissionsService;

	private final ProducerService producerService;

	@Scheduled(fixedRate = 300000) // 5 minutes
	private void sheduledFileCheck() {
		var data = appDataDAO.getAppData();

		if (data.getFileState().equals(FileState.NO_FILE_IN_QUEUE)) {
			try {
				var forwardMessage = fileCheckSender.sendFileForCheck();
				producerService.produceAnswer(forwardMessage);
			} catch (FileCheckException e) {

			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

	@Scheduled(cron = "0 0 0 * * *")
	private void sheduledWipeUserData() {
		List<AppUser> users = appUserDAO.findAll();
		for(AppUser currentUser : users) {
			permissionsService.wipeUserPermissions(currentUser);
		}
	}
}
