package ua.gexlq.TelegramStudyBot.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.gexlq.TelegramStudyBot.entity.DocumentMetadata;

public interface DocumentMetadataDAO extends JpaRepository<DocumentMetadata, Long> {
}
