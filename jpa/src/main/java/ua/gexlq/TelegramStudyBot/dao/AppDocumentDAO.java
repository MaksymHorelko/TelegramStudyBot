package ua.gexlq.TelegramStudyBot.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.gexlq.TelegramStudyBot.entity.AppDocument;

public interface AppDocumentDAO extends JpaRepository<AppDocument, Long> {

	public Optional<AppDocument> findById(Long id);

	@Query(value = "SELECT * FROM app_document ORDER BY id DESC LIMIT 1", nativeQuery = true)
	public AppDocument findLastDocument();

	public default List<AppDocument> findDocumentByWorkCode(String code) {

		List<AppDocument> appDocumentList = findAll();
		List<AppDocument> findedDocuments = new ArrayList<>();

		for (AppDocument appDocument : appDocumentList) {

			if (appDocument.getDocumentMetadata().getWorkCode().equals(code)) {
				findedDocuments.add(appDocument);
			}

		}

		return findedDocuments;
	}
}
