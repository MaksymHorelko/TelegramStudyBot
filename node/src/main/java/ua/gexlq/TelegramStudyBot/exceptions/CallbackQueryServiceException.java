package ua.gexlq.TelegramStudyBot.exceptions;

public class CallbackQueryServiceException extends RuntimeException {
	public CallbackQueryServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CallbackQueryServiceException(String message) {
		super(message);
	}

	public CallbackQueryServiceException(Throwable cause) {
		super(cause);
	}
}
