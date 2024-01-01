package ua.gexlq.TelegramStudyBot.model;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import ua.gexlq.TelegramStudyBot.handler.Logger;

@Component
public class UserManager {

	private String defaultLanguage = "ua";
	
	Repository repository;

	public UserManager(Repository repository) {
		this.repository = repository;
	}

	public void registerUser(Update update) {

		if (repository.findById(update.getMessage().getChatId()).isEmpty()) {

			var chatId = update.getMessage().getChatId();

			var chat = update.getMessage().getChat();

			User user = new User();

			user.setChatId(chatId);

			user.setNickName(chat.getUserName());
			user.setFirtName(chat.getFirstName());
			user.setLastName(chat.getLastName());

			user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

			user.setLanguage(defaultLanguage);

			Logger.logUser(user);
			
			repository.save(user);
		}
	}

	public String getUserNickName(long userId) {
		Optional<User> user = repository.findById(userId);
		return user.get().getNickName();
	}

	public String getUserFirstName(long userId) {
		Optional<User> user = repository.findById(userId);
		return user.get().getFirtName();
	}

	public String getUserLastName(long userId) {
		Optional<User> user = repository.findById(userId);
		return user.get().getLastName();
	}

	public String getUserLanguage(long userId) {
		Optional<User> user = repository.findById(userId);
		return user.get().getLanguage();
	}

	public Timestamp getUserRegisterAt(long userId) {
		Optional<User> user = repository.findById(userId);
		return user.get().getRegisteredAt();
	}

}
