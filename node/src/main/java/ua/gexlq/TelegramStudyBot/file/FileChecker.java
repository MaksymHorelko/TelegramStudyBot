package ua.gexlq.TelegramStudyBot.file;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.utils.ArchiveChecker;
import ua.gexlq.TelegramStudyBot.utils.VirusTotal;

@RequiredArgsConstructor
@Component
public class FileChecker {

	private final VirusTotal virusTotal;
	private final ArchiveChecker archiveChecker;
	private final FileDeleter fileDeleter;

	public boolean isFileSafe(String filePath) {
		try {
			boolean containsViruses = virusTotal.isFileContainsViruses(filePath);

			if (containsViruses) {
				fileDeleter.deleteFileFromSystem(filePath);
				return false;
			}

			if (archiveChecker.isArchive(filePath)) {
				boolean unsupportedArchive = archiveChecker.isSafeArchive(filePath);

				if (unsupportedArchive) {
					fileDeleter.deleteFileFromSystem(filePath);
					return false;
				}
			}

			return true;

		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: ", e);
		}
	}
}
