package ua.gexlq.TelegramStudyBot.exceptions;

public class FileServiceException extends RuntimeException {
	private static final long serialVersionUID = -7501995849064059209L;

	public FileServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileServiceException(String message) {
		super(message);
	}

	public FileServiceException(Throwable cause) {
		super(cause);
	}
}
