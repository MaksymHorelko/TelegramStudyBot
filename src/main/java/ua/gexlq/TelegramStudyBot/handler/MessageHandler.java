package ua.gexlq.TelegramStudyBot.handler;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageHandler {
	public static String getMessage(String messageCode, String language) {

		Locale current = new Locale(language);
		ResourceBundle rb = ResourceBundle.getBundle("messages", current);
		
		if(!rb.containsKey(messageCode)) {
			return null;
		}
		
		return rb.getString(messageCode);
	}
}
