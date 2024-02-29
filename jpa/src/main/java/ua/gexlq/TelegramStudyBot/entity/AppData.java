package ua.gexlq.TelegramStudyBot.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.gexlq.TelegramStudyBot.entity.enums.FileState;

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
	private final int maxWarningsBeforeBan = 5;
	
	@Builder.Default
	private final int maxContactsPerDay = 3;
	
	@Builder.Default
	private final Integer maxUploadedFilesPerDay = 5;
	
	@Builder.Default
	private final Integer maxUploadedFileSize = 20_000_000;
	
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private FileState fileState = FileState.NO_FILE_IN_QUEUE;

}