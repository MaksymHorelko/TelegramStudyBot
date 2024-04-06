package ua.gexlq.TelegramStudyBot.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.gexlq.TelegramStudyBot.entity.RawData;

public interface RawDataDAO extends JpaRepository<RawData, Long>{
}
