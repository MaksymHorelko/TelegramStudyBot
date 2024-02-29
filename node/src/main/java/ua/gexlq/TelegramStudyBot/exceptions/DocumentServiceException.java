package ua.gexlq.TelegramStudyBot.exceptions;

public class DocumentServiceException extends RuntimeException {
	public DocumentServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public DocumentServiceException(String message) {
		super(message);
	}

	public DocumentServiceException(Throwable cause) {
		super(cause);
	}
}
