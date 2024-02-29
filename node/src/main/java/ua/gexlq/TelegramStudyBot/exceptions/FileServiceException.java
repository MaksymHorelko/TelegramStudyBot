package ua.gexlq.TelegramStudyBot.exceptions;

public class FileServiceException extends RuntimeException {
	public FileServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileServiceException(String message) {
		super(message);
	}

	public FileServiceException(Throwable cause) {
		super(cause);
	}
}
