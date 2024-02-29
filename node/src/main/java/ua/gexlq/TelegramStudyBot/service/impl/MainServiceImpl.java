package ua.gexlq.TelegramStudyBot.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.gexlq.TelegramStudyBot.dao.AppDataDAO;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.dao.RawDataDAO;
import ua.gexlq.TelegramStudyBot.entity.AppUser;
import ua.gexlq.TelegramStudyBot.entity.RawData;
import ua.gexlq.TelegramStudyBot.entity.enums.UserState;
import ua.gexlq.TelegramStudyBot.entity.service.AppDataService;
import ua.gexlq.TelegramStudyBot.exceptions.DocumentServiceException;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.CallbackQueryMessageService;
import ua.gexlq.TelegramStudyBot.process.document.DocumentMessageService;
import ua.gexlq.TelegramStudyBot.process.text.TextMessageService;
import ua.gexlq.TelegramStudyBot.service.MainService;

@Slf4j
@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService {

	// DAO
	private final RawDataDAO rawDataDAO;
	private final AppUserDAO appUserDAO;
	private final AppDataDAO appDataDAO;

	// Services
	private final AppDataService appDataService;
	private final TextMessageService textMessageService;
	private final CallbackQueryMessageService callbackQueryMessageService;
	private final DocumentMessageService documentMessageService;

	@PostConstruct
	private void initAppData() {
		appDataService.initializeAppData();
	}

	@Override
	public void processTextMessage(Update update) {
		try {
			saveRawData(update);
			var user = findOrSaveAppUser(update);

			if (textMessageService.isCheckGroupChat(update)) {
				textMessageService.handleCheckGroupChat(update);
				return;
			}

			textMessageService.handleUserChat(user, update);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void processCallbackQueryMessage(Update update) {
		try {
			saveRawData(update);

			var user = findOrSaveAppUser(update);

			callbackQueryMessageService.handleContactPermission(update);

			callbackQueryMessageService.handleFileUploadPermission(update);

			callbackQueryMessageService.handleCurrentActiveMessageId(user, update);

			callbackQueryMessageService.handleUserCallBackData(update);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void processDocMessage(Update update) {
		try {
			saveRawData(update);

			findOrSaveAppUser(update);

			if (documentMessageService.isSoftwareDocument(update)) {
				documentMessageService.handleSoftware(update);
				return;
			}

			documentMessageService.handleUserDocument(update);

		} catch (DocumentServiceException docEx) {
			log.info(docEx.getMessage());
		} catch (Exception e) {
			log.error("Error occurred", e);
		}
	}

	private AppUser findOrSaveAppUser(Update update) {
		User telegramUser;

		if (update.hasMessage())
			telegramUser = update.getMessage().getFrom();
		else
			telegramUser = update.getCallbackQuery().getFrom();

		AppUser persistenceUser = appUserDAO.findUserByTelegramUserId(telegramUser.getId());

		if (persistenceUser == null) {
			AppUser transietAppUser = AppUser.builder().telegramUserId(telegramUser.getId())
					.firstName(telegramUser.getFirstName()).lastName(telegramUser.getLastName())
					.nickName(telegramUser.getUserName()).build();
			if (update.getMessage().getChatId() < 0)
				transietAppUser.setUserState(UserState.GROUP_STATE);

			var appData = appDataDAO.getAppData();
			appData.setUsers(appData.getUsers() + 1);
			appDataDAO.save(appData);

			log.info("User saved: " + transietAppUser);
			return appUserDAO.save(transietAppUser);
		}

		return persistenceUser;
	}

	public void saveRawData(Update update) {
		RawData rawData = RawData.builder().event(update).build();

		rawDataDAO.save(rawData);
	}
}
