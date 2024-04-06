package ua.gexlq.TelegramStudyBot.exceptions;

public class UnsafeFileException extends RuntimeException {
	private static final long serialVersionUID = 2090828835843704387L;

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
