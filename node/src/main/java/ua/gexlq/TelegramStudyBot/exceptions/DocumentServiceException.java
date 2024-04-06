package ua.gexlq.TelegramStudyBot.exceptions;

public class DocumentServiceException extends RuntimeException {
	private static final long serialVersionUID = 1615476598221411691L;

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
