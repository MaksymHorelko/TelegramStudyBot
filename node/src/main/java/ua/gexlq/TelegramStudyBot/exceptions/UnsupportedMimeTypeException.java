package ua.gexlq.TelegramStudyBot.exceptions;

public class UnsupportedMimeTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnsupportedMimeTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedMimeTypeException(String message) {
		super(message);
	}

	public UnsupportedMimeTypeException(Throwable cause) {
		super(cause);
	}
}
