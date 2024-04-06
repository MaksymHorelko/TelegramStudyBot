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

	public void addNewWarning(AppUser appUser) {
		var newPermissions = appUser.getPermissions();

		int newNumWarnings = newPermissions.getWarnings() + 1;

		if (newNumWarnings >= appDataDAO.getAppData().getMaxWarningsBeforeBan()) {
			newPermissions.setTrusted(false);
		}

		newPermissions.setWarnings(newNumWarnings);
		appUser.setPermissions(newPermissions);
		appUserDAO.save(appUser);
	}

	public void addNewWarning(Update update) {
		addNewWarning(getAppUserByChatId(update));
	}

	public void addNewWarning(long chatId) {
		addNewWarning(getAppUserByChatId(chatId));
	}

	public void addNewUpload(AppUser appUser) {
		var userPermissions = appUser.getPermissions();
		userPermissions.setUploadedFilesToday(userPermissions.getUploadedFilesToday() + 1);
		appUserDAO.save(appUser);
	}

	public void addNewUpload(Update update) {
		addNewUpload(getAppUserByChatId(update));
	}

	public void addNewUpload(long chatId) {
		addNewUpload(getAppUserByChatId(chatId));
	}

	public void addNewContact(AppUser appUser) {
		var userPermissions = appUser.getPermissions();

		userPermissions.setContactsToday(userPermissions.getContactsToday() + 1);
		appUser.setPermissions(userPermissions);
		appUserDAO.save(appUser);
	}

	public void addNewContact(Update update) {
		addNewContact(getAppUserByChatId(update));
	}

	public void addNewContact(long chatId) {
		addNewContact(getAppUserByChatId(chatId));
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

	public void wipeUserPermissions(AppUser appUser) {
		var newPermissions = appUser.getPermissions();
		
		newPermissions.setContactsToday(0);
		newPermissions.setUploadedFilesToday(0);
		appUser.setPermissions(newPermissions);
		appUserDAO.save(appUser);
	}
	
	public AppUser getAppUserByChatId(Update update) {
		User telegramUser;

		if (update.hasMessage())
			telegramUser = update.getMessage().getFrom();
		else
			telegramUser = update.getCallbackQuery().getFrom();

		return appUserDAO.findUserByTelegramUserId(telegramUser.getId());
	}

	public AppUser getAppUserByChatId(long chatId) {
		return appUserDAO.findUserByTelegramUserId(chatId);
	}
}
