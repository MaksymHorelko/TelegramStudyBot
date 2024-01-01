package ua.gexlq.TelegramStudyBot.handler;


import lombok.extern.slf4j.Slf4j;
import ua.gexlq.TelegramStudyBot.model.User;


@Slf4j
public class Logger {
	
	public static void logUser(User user) {
		log.info("user saved: " + user);
	}
	
	public static void logError(Exception e) {
		log.error(e.getMessage());
	}
	
	
}
