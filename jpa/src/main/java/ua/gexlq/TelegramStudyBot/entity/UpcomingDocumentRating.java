package ua.gexlq.TelegramStudyBot.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class UpcomingDocumentRating {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String workCode;

	private String rateContent = "0 %";

	private String rateImplementaion = "0 %";

	private String rateMark = "0 %";
}
