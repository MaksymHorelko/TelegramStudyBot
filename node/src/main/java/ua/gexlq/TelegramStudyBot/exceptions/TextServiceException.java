package ua.gexlq.TelegramStudyBot.exceptions;

public class TextServiceException extends RuntimeException {
	private static final long serialVersionUID = 6643606114148068819L;

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
