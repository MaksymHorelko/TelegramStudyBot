package ua.gexlq.TelegramStudyBot.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.gexlq.TelegramStudyBot.entity.AppUser;

public interface AppUserDAO extends JpaRepository<AppUser, Long> {
	public AppUser findUserByTelegramUserId(Long id);
}
