package ua.gexlq.TelegramStudyBot.process.document.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppSoftwareDAO;
import ua.gexlq.TelegramStudyBot.entity.AppSoftware;

@RequiredArgsConstructor
@Component
public class ProcessSoftware {

	private final AppSoftwareDAO appSoftwareDAO;

	@Value("${folder.softwareGroupId}")
	private String softwareGroupId;

	public void handle(Update update) {
		String fileName = update.getMessage().getDocument().getFileName();
		long messageId = update.getMessage().getMessageId();
		AppSoftware newSoftware = AppSoftware.builder().softwareName(fileName).messageIdInSoftwareFolder(messageId)
				.build();
		appSoftwareDAO.save(newSoftware);
	}

	public boolean isSupportedType(Update update) {
		return update.getMessage().getChatId().equals(Long.valueOf(softwareGroupId))
				&& update.getMessage().hasDocument();
	}

}
