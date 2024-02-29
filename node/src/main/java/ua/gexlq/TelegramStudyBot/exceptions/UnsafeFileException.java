package ua.gexlq.TelegramStudyBot.exceptions;

public class UnsafeFileException extends RuntimeException {
	public UnsafeFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsafeFileException(String message) {
		super(message);
	}

	public UnsafeFileException(Throwable cause) {
		super(cause);
	}
}
