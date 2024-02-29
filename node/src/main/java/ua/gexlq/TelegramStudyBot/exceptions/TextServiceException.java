package ua.gexlq.TelegramStudyBot.exceptions;

public class TextServiceException extends RuntimeException {
	public TextServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public TextServiceException(String message) {
		super(message);
	}

	public TextServiceException(Throwable cause) {
		super(cause);
	}
}
