package ua.gexlq.TelegramStudyBot.exceptions;

public class FileCheckException extends RuntimeException {
	private static final long serialVersionUID = -5318342828558274709L;

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
