package ua.gexlq.TelegramStudyBot.exceptions;

public class UnsupportedMimeTypeException extends RuntimeException {
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
