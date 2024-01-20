package ua.gexlq.TelegramStudyBot.model;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import ua.gexlq.TelegramStudyBot.model.UserService.UserState;

@Data
@Entity(name = "dataTable")
public class User {
	@Id
	private Long chatId;

	private String firstName;

	private String lastName;

	private String nickName;

	private String semester;

	private String faculty;

	private String specialization;

	private String language;

	@Enumerated(EnumType.STRING)
	private UserState userState;

	private Timestamp registeredAt;

	@Override
	public String toString() {
		return "User:{" + "chatID = " + chatId + " | " +

				"nickName = " + nickName + " | " + "firstName = " + firstName + " | " + "lastName = " + lastName + " | "
				+

				"semester = " + semester + " | " + "faculty  = " + faculty + " | " + "specialization = "
				+ specialization + " | " +

				"atMenu = " + userState + " | " + "language = " + language + " | " + "registeredAt = " + registeredAt
				+ "}";

	}
}
