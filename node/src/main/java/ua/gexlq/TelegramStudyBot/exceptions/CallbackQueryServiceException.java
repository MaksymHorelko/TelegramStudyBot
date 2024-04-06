package ua.gexlq.TelegramStudyBot.exceptions;

public class CallbackQueryServiceException extends RuntimeException {
	private static final long serialVersionUID = -3633575165983589191L;

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
