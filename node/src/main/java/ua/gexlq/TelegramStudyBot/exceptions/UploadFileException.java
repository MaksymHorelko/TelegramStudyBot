package ua.gexlq.TelegramStudyBot.exceptions;

public class UploadFileException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UploadFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public UploadFileException(String message) {
		super(message);
	}

	public UploadFileException(Throwable cause) {
		super(cause);
	}
}
