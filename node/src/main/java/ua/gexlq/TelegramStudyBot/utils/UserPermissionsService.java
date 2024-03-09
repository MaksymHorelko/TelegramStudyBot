package ua.gexlq.TelegramStudyBot.utils;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppDataDAO;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.entity.AppUser;

@RequiredArgsConstructor
@Service
public class UserPermissionsService {

	private final AppUserDAO appUserDAO;
	private final AppDataDAO appDataDAO;

	public void addNewWarning(Update update) {
		var user = getAppUserByChatId(update);
		var newPermissions = user.getPermissions();

		int newNumWarnings = newPermissions.getWarnings() + 1;

		if (newNumWarnings >= appDataDAO.getAppData().getMaxWarningsBeforeBan()) {
			newPermissions.setTrusted(false);
		}

		newPermissions.setWarnings(newNumWarnings);
		user.setPermissions(newPermissions);
		appUserDAO.save(user);
	}

	public void addNewWarning(long chatId) {
		var user = getAppUserByChatId(chatId);
		var newPermissions = user.getPermissions();

		int newNumWarnings = newPermissions.getWarnings() + 1;

		if (newNumWarnings >= appDataDAO.getAppData().getMaxWarningsBeforeBan()) {
			newPermissions.setTrusted(false);
		}

		newPermissions.setWarnings(newNumWarnings);
		user.setPermissions(newPermissions);
		appUserDAO.save(user);
	}

	public AppUser addNewUpload(Update update) {
		var user = appUserDAO.findUserByTelegramUserId(update.getMessage().getChatId());
		var userPermissions = user.getPermissions();
		userPermissions.setUploadedFilesToday(userPermissions.getUploadedFilesToday() + 1);
		return appUserDAO.save(user);
	}

	public AppUser addNewContact(Update update) {
		var user = appUserDAO.findUserByTelegramUserId(update.getMessage().getChatId());
		var userPermissions = user.getPermissions();

		userPermissions.setContactsToday(userPermissions.getContactsToday() + 1);
		user.setPermissions(userPermissions);
		return appUserDAO.save(user);
	}

	public void setFileUploadPermission(Update update, boolean isAllowed) {
		var user = getAppUserByChatId(update);
		var newPermissions = user.getPermissions();

		newPermissions.setAbleToSendFile(isAllowed);
		user.setPermissions(newPermissions);
		appUserDAO.save(user);
	}

	public void setContactPermission(Update update, boolean isAllowed) {
		var user = getAppUserByChatId(update);
		var newPermissions = user.getPermissions();

		newPermissions.setAbleToSendMesToSupport(isAllowed);
		user.setPermissions(newPermissions);
		appUserDAO.save(user);
	}

	private AppUser getAppUserByChatId(Update update) {
		User telegramUser;

		if (update.hasMessage())
			telegramUser = update.getMessage().getFrom();
		else
			telegramUser = update.getCallbackQuery().getFrom();

		return appUserDAO.findUserByTelegramUserId(telegramUser.getId());
	}

	private AppUser getAppUserByChatId(long chatId) {
		return appUserDAO.findUserByTelegramUserId(chatId);
	}
}
