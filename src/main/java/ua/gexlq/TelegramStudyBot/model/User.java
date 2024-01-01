package ua.gexlq.TelegramStudyBot.model;


import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "dataTable")
public class User {
	@Id
	private Long chatId;

	private String firtName;
	
	private String lastName;
	
	private String nickName;
	
	private String language;

	private Timestamp registeredAt;
	
	@Override
	public String toString() {
		return "User:{" +
				"chatID = " + chatId+ " " +
				"nickName = " + nickName +" " +
				"firstName = " + firtName +" " +
				"lastName = " + lastName +" " +
				"language = " + language + " " +
				"registeredAt = " + registeredAt +
				"}";
		
	}
}
