package ua.gexlq.TelegramStudyBot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppDataDAO;
import ua.gexlq.TelegramStudyBot.dao.DownloadedFileDAO;
import ua.gexlq.TelegramStudyBot.entity.enums.FileState;
import ua.gexlq.TelegramStudyBot.exceptions.FileCheckException;

@RequiredArgsConstructor
@Component
public class FileCheckSender {

	@Value("${folder.checkGroupId}")
	private String checkGroupId;

	@Value("${folder.tempGroupId}")
	private String tempGroupId;

	private final AppDataDAO appDataDAO;

	private final DownloadedFileDAO downloadedFileDAO;

	private final MessageUtils messageUtils;

	public ForwardMessage sendFileForCheck() {
		var data = appDataDAO.getAppData();

		long currentFileIdForCheck = data.getFileOnCheck() + 1;

		var downloadedFile = downloadedFileDAO.findDownloadedFileById(currentFileIdForCheck);

		if (downloadedFile == null)
			throw new FileCheckException("File not found by id: " + currentFileIdForCheck + " for check");

		long tempMessageIdToForward = downloadedFile.getMessageIdInTemp();

		var forwardMessage = messageUtils.createForwardMessageDocument(Long.valueOf(tempGroupId),
				Long.valueOf(checkGroupId), tempMessageIdToForward);

		data.setFileOnCheck(currentFileIdForCheck);
		data.setFileState(FileState.WAITING_FOR_RESPOND);
		appDataDAO.save(data);

		return forwardMessage;
	}
}
