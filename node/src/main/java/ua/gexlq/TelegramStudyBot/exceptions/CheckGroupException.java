package ua.gexlq.TelegramStudyBot.exceptions;

public class CheckGroupException extends RuntimeException {
	public CheckGroupException(String message, Throwable cause) {
		super(message, cause);
	}

	public CheckGroupException(String message) {
		super(message);
	}

	public CheckGroupException(Throwable cause) {
		super(cause);
	}
}
