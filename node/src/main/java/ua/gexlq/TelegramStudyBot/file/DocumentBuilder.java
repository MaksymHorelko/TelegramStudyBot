package ua.gexlq.TelegramStudyBot.file;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.dao.DocumentMetadataDAO;
import ua.gexlq.TelegramStudyBot.entity.AppUser;
import ua.gexlq.TelegramStudyBot.entity.DocumentMetadata;
import ua.gexlq.TelegramStudyBot.entity.DownloadedFile;

@RequiredArgsConstructor
@Component
public class DocumentBuilder {

	private final AppUserDAO appUserDAO;
	private final DocumentMetadataDAO documentInfoDAO;

	public DownloadedFile buildDownloadedFile(Update update, String filePath) {

		var document = update.getMessage().getDocument();

		Long authorId = update.getMessage().getFrom().getId();

		String authorUserName = update.getMessage().getFrom().getUserName();

		String docName = filePath.substring(filePath.lastIndexOf("/") + 1);

		String telegramFileId = document.getFileId();

		String mimeType = document.getMimeType();

		Long fileSize = Long.valueOf(document.getFileSize());

		AppUser user = appUserDAO.findUserByTelegramUserId(authorId);
		var documentCodeAndRating = user.getUpcomingDocument();

		String workCode = documentCodeAndRating.getWorkCode();
		String rateContent = documentCodeAndRating.getRateContent();
		String rateImplementaion = documentCodeAndRating.getRateImplementaion();
		String rateMark = documentCodeAndRating.getRateMark();

		var documentMetadata = DocumentMetadata.builder().authorId(authorId).authorUserName(authorUserName)
				.workCode(workCode).rateContent(rateContent).rateImplementaion(rateImplementaion).rateMark(rateMark)
				.docName(docName).telegramFileId(telegramFileId).mimeType(mimeType).fileSize(fileSize).build();
		documentInfoDAO.save(documentMetadata);

		return DownloadedFile.builder().documentMetadata(documentMetadata).filePath(filePath).build();
	}
}
