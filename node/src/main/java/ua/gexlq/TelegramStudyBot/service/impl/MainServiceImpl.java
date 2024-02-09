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
import ua.gexlq.TelegramStudyBot.process.callBackData.ProcessCallBackDataStateManager;
import ua.gexlq.TelegramStudyBot.process.callBackData.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.process.text.ProcessStateManager;
import ua.gexlq.TelegramStudyBot.service.FileService;
import ua.gexlq.TelegramStudyBot.service.MainService;
import ua.gexlq.TelegramStudyBot.service.ProducerService;
import ua.gexlq.TelegramStudyBot.service.enums.SupportedMimeType;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService {

	private final RawDataDAO rawDataDAO;

	private final AppUserDAO appUserDAO;

	private final AppDataDAO appDataDAO;

	private final AppDataService appDataService;

	private final ProducerService producerService;

	private final FileService fileService;

	private final ProcessStateManager processStateManager;

	private final ProcessCallBackDataStateManager processCallBackDataStateManager;

	private final MessageUtils messageUtils;

	@PostConstruct
	private void initAppData() {
		appDataService.initializeAppData();
	}

	@Override
	public void processTextMessage(Update update) {
		saveRawData(update);

		var user = findOrSaveAppUser(update);

		if (user.getIsReadyToSendFile())
			setFileUploadPermission(update, false);

		var response = processStateManager.handle(user.getUserState(), update);

		producerService.produceAnswer(response);
	}

	@Override
	public void processDocMessage(Update update) {

		var user = findOrSaveAppUser(update);

		if (!user.getIsReadyToSendFile()) {

			// TODO forward message to trash group for check

			return;
		}

		if (update.getMessage().getDocument().getFileSize() > 20_000_000) {
			var tooBig = messageUtils.createSendMessageWithAnswerCode(update, "message.upload.fileIsTooBig");
			producerService.produceAnswer(tooBig);

			// TODO forward message to trash group for check

			setFileUploadPermission(update, false);

			return;
		}

		String mimeType = update.getMessage().getDocument().getMimeType();

		if (!SupportedMimeType.isSupportedMimeType(mimeType)) {
			var fileDoesntSupported = messageUtils.createSendMessageWithAnswerCode(update,
					"message.upload.fileDoesntSupported");
			producerService.produceAnswer(fileDoesntSupported);

			// TODO forward message to trash group for check

			setFileUploadPermission(update, false);

			return;
		}

		saveRawData(update);

		var processing = messageUtils.createSendMessageWithAnswerCode(update, "message.upload.processing");
		producerService.produceAnswer(processing);

		var downloadedDocument = fileService.downloadDocument(update);

		var uploadedSuccess = messageUtils.createSendMessageWithAnswerCode(update, "message.upload.success");
		producerService.produceAnswer(uploadedSuccess);

		// TODO send file to scanclass

		// if (scan result is good) - >
		// TODO send file to temp
		// producerService.produceAnswer(downloadedDocument);

		// else
		// TODO delete file

		setFileUploadPermission(update, false);
	}

	@Override
	public void processCallBackDataMessage(Update update) {
		saveRawData(update);

		var user = findOrSaveAppUser(update);

		if (user.getIsReadyToSendFile())
			setFileUploadPermission(update, false);

		if (user.getCurrentActiveMessageId() != null && !user.getCurrentActiveMessageId()
				.equals(String.valueOf(update.getCallbackQuery().getMessage().getMessageId()))) {
			update.getCallbackQuery().setData(CallBackDataTypes.ERROR.toString());
		}

		var response = processCallBackDataStateManager.handle(update);

		producerService.produceAnswer(response);
	}

	public void setFileUploadPermission(Update update, boolean isAllowed) {
		User telegramUser;

		if (update.hasMessage())
			telegramUser = update.getMessage().getFrom();
		else
			telegramUser = update.getCallbackQuery().getFrom();

		AppUser user = appUserDAO.findUserByTelegramUserId(telegramUser.getId());

		user.setIsReadyToSendFile(isAllowed);

		appUserDAO.save(user);
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
					.nickName(telegramUser.getUserName()).telegramChatId(update.getMessage().getChatId()).build();
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
