package ua.gexlq.TelegramStudyBot.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;

@Component
@Configuration
@PropertySource("application.properties")
public class RequestHandler {

//	@Value("${suggestionId}")
//	private long suggestionId;

	// messageId -> fileName
	private Map<Integer, String> fileMap;

	@Value("${folderId}")
	private long folderId;

	public SendDocument getDocument(long chatId, String fileName) {

		return null;
	}
	
	public void reloadFiles() {
		
		
		
		
	}
}
