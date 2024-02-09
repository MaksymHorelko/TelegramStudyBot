package ua.gexlq.TelegramStudyBot.utils;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;

@RequiredArgsConstructor
@Component
public class UserInfo {
	private final AppUserDAO appUserDAO;

	public long getUserId(long telegramUserId) {
		return appUserDAO.findUserByTelegramUserId(telegramUserId).getId();
	}

	public String getUserFirstName(long telegramUserId) {
		return appUserDAO.findUserByTelegramUserId(telegramUserId).getFirstName();
	}

	public String getUserLastName(long telegramUserId) {
		return appUserDAO.findUserByTelegramUserId(telegramUserId).getLastName();
	}

	public String getUserNickName(long telegramUserId) {
		return appUserDAO.findUserByTelegramUserId(telegramUserId).getNickName();
	}

	public LocalDateTime getUserRegisterDate(long telegramUserId) {
		return appUserDAO.findUserByTelegramUserId(telegramUserId).getRegisterDate();
	}

	public String getUserFaculty(long telegramUserId) {
		return appUserDAO.findUserByTelegramUserId(telegramUserId).getFaculty();
	}

	public String getUserSpecialization(long telegramUserId) {
		return appUserDAO.findUserByTelegramUserId(telegramUserId).getSpecialization();
	}

	public String getUserSemester(long telegramUserId) {
		return appUserDAO.findUserByTelegramUserId(telegramUserId).getSemester();
	}

	public String getUserLanguage(long telegramUserId) {
		var user = appUserDAO.findUserByTelegramUserId(telegramUserId);
		return user.getUserLanguage();
	}

	public boolean isUserFacultySet(long telegramUserId) {
		return getUserFaculty(telegramUserId) == null;
	}

	public boolean isUserSpecializationSet(long telegramUserId) {
		return getUserSpecialization(telegramUserId) == null;
	}

	public boolean isUserSemesterSet(long telegramUserId) {
		return getUserSemester(telegramUserId) == null;
	}

	public boolean isUserDataSet(long telegramUserId) {
		return getUserSemester(telegramUserId) != null;
	}

}
