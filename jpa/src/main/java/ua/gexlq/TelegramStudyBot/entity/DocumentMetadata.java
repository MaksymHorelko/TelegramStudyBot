package ua.gexlq.TelegramStudyBot.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "document_metadata")
public class DocumentMetadata {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long authorId;

	private String authorUserName;

	private String workCode;
	
	private String rateContent;

	private String rateImplementaion;

	private String rateMark;

	private String telegramFileId;

	private String docName;

	private String mimeType;

	private Long fileSize;
}
