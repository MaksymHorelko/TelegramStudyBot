package ua.gexlq.TelegramStudyBot.exceptions;

public class FileCheckException extends RuntimeException {
	public FileCheckException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileCheckException(String message) {
		super(message);
	}

	public FileCheckException(Throwable cause) {
		super(cause);
	}
}
