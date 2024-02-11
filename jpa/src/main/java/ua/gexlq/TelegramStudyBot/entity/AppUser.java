package ua.gexlq.TelegramStudyBot.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.gexlq.TelegramStudyBot.entity.enums.UserState;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "app_user")
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long telegramUserId;

	private String firstName;

	private String lastName;

	private String nickName;

	private String faculty;

	private String specialization;

	private String semester;
	
	// LAST USER MESSAGE ID
	private String currentActiveMessageId;

	@Builder.Default
	private Integer warnings = 0;

	@Builder.Default
	private Boolean isReadyToSendFile = false;

	@Builder.Default
	private Boolean isReadyToSendSuggestion = false;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	private UserState userState = UserState.MAIN_STATE;

	@CreationTimestamp
	private LocalDateTime registerDate;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "upcoming_document_rating_id")
	private UpcomingDocumentRating upcomingDocumentRating;

	@Builder.Default
	private String userLanguage = "uk";
}