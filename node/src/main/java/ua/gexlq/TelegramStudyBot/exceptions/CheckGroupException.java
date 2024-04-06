package ua.gexlq.TelegramStudyBot.exceptions;

public class CheckGroupException extends RuntimeException {
	private static final long serialVersionUID = -243697036650839240L;

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
