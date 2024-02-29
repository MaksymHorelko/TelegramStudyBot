package ua.gexlq.TelegramStudyBot.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class UserPermissions {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Builder.Default
	private Integer warnings = 0;

	@Builder.Default
	private Boolean trusted = true;
	
	@Builder.Default
	private Boolean ableToSendFile = false;

	@Builder.Default
	private Boolean ableToSendMesToSupport = false;
	
	@Builder.Default
	private Integer contactsToday = 0;
	
	@Builder.Default
	private Integer uploadedFilesToday = 0;
	
}
