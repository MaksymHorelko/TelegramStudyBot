package ua.gexlq.TelegramStudyBot.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "app_data")
public class AppData {

	@Id
	@Builder.Default
	private Long id = 1L;
	
	@Builder.Default
	private int users = 0;

	@Builder.Default
	private int uploadedWorks = 0;

	@Builder.Default
	private long fileOnCheck = 0;
	
	@Builder.Default
	private long lastFolderMessageId = 0;

	@Builder.Default
	private long lastCheckMessageId = 0;

	@Builder.Default
	private long lastTempMessageId = 0;

	@Builder.Default
	private long lastSuggestionMessageId = 0;

}