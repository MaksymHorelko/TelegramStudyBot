package ua.gexlq.TelegramStudyBot.process.document;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppDataDAO;
import ua.gexlq.TelegramStudyBot.exceptions.DocumentServiceException;
import ua.gexlq.TelegramStudyBot.exceptions.UnsafeFileException;
import ua.gexlq.TelegramStudyBot.process.document.impl.ProcessDocument;
import ua.gexlq.TelegramStudyBot.process.document.impl.ProcessSoftware;
import ua.gexlq.TelegramStudyBot.service.ProducerService;
import ua.gexlq.TelegramStudyBot.service.enums.SupportedMimeType;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;
import ua.gexlq.TelegramStudyBot.utils.UserPermissionsService;

// TODO clean code
@RequiredArgsConstructor
@Service
public class DocumentMessageService {

	private final ProcessSoftware processSoftware;
	private final ProcessDocument processDocument;
	private final ProducerService producerService;
	private final AppDataDAO appDataDAO;
	private final UserInfo userInfo;
	private final UserPermissionsService permissionsService;
	private final MessageUtils messageUtils;

	private final String MESSAGE_FILE_NOT_DEFIEND = "message.fileNotDefined";
	private final String MESSAGE_USER_NOT_TRUSTED = "message.userNotTrusted";

	private final String MESSAGE_UPLOAD_FILE_DOESNT_SUPPORTED = "message.upload.fileDoesntSupported";
	private final String MESSAGE_UPLOAD_FILE_IS_NOT_SAFE = "message.upload.fileIsNotSafe";
	private final String MESSAGE_UPLOAD_FILE_IS_TOO_BIG = "message.upload.fileIsTooBig";

	private final String MESSAGE_UPLOAD_SUCCESS = "message.upload.success";
	private final String MESSAGE_UPLOAD_PROCESSING = "message.upload.processing";

	public void handleUserDocument(Update update) {
		try {
			checkUserPermissions(update);
			checkFileCorrection(update);

			var processing = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UPLOAD_PROCESSING);
			producerService.produceAnswer(processing);

			// Send file to TEMP group
			producerService.produceAnswer(processDocument.handle(update));

			var uploadedSuccess = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UPLOAD_SUCCESS);
			producerService.produceAnswer(uploadedSuccess);

		} catch (UnsafeFileException e) {
			var fileIsNotSafe = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UPLOAD_FILE_IS_NOT_SAFE);
			producerService.produceAnswer(fileIsNotSafe);
		} catch (Exception e) {
			throw e;
		}
	}

	public void handleSoftware(Update update) {
		processSoftware.handle(update);
	}

	public boolean isSoftwareDocument(Update update) {
		return processSoftware.isSupportedType(update);
	}

	private void checkUserPermissions(Update update) {
		handleContactPermission(update);
		checkUserTrust(update);
		checkUserFileSendingAbility(update);
	}

	private void checkFileCorrection(Update update) {
		checkFileSize(update);
		checkMimeType(update);
	}

	private void checkUserTrust(Update update) {
		if (userInfo.isUserTrusted(update.getMessage().getChatId()))
			return;

		var isNotTrusted = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_USER_NOT_TRUSTED);
		producerService.produceAnswer(isNotTrusted);
		throw new DocumentServiceException("User: " + update.getMessage().getChatId() + " now is no longer trusted");
	}

	private void checkUserFileSendingAbility(Update update) {
		if (userInfo.isUserAbleToSendFile(update.getMessage().getChatId()))
			return;

		var cantSendFile = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_FILE_NOT_DEFIEND);
		producerService.produceAnswer(cantSendFile);
		throw new DocumentServiceException("User " + update.getMessage().getChatId() + " not able to send files");
	}

	private void checkFileSize(Update update) {
		if (!isFileSizeTooBig(update))
			return;

		var tooBig = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UPLOAD_FILE_IS_TOO_BIG);
		producerService.produceAnswer(tooBig);
		permissionsService.setFileUploadPermission(update, false);
		throw new DocumentServiceException("Uploaded file is too big");
	}

	private void checkMimeType(Update update) {
		if (isSupportedMimeType(update))
			return;

		var fileDoesntSupported = messageUtils.createSendMessageWithAnswerCode(update,
				MESSAGE_UPLOAD_FILE_DOESNT_SUPPORTED);
		producerService.produceAnswer(fileDoesntSupported);
		permissionsService.setFileUploadPermission(update, false);
		throw new DocumentServiceException("Unsupported mime type: " + update.getMessage().getDocument().getMimeType());
	}

	private void handleContactPermission(Update update) {
		if (userInfo.isUserAbleToSendMesToSupport(update.getMessage().getChatId())) {
			permissionsService.setContactPermission(update, false);
		}
	}

	private boolean isFileSizeTooBig(Update update) {
		return update.getMessage().getDocument().getFileSize() > appDataDAO.getAppData().getMaxUploadedFileSize();
	}

	private boolean isSupportedMimeType(Update update) {
		String mimeType = update.getMessage().getDocument().getMimeType();
		return SupportedMimeType.isSupportedMimeType(mimeType);
	}
}
