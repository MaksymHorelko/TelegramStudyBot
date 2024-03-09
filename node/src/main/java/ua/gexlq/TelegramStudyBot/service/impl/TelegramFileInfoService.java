package ua.gexlq.TelegramStudyBot.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ua.gexlq.TelegramStudyBot.service.FileInfoService;

@Component
public class TelegramFileInfoService implements FileInfoService {

	@Value("${token}")
	private String token;

	@Value("${service.file_info.uri}")
	private String fileInfoUri;

	@Override
	public ResponseEntity<String> getFilePath(String fileId) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> request = new HttpEntity<>(headers);

		return restTemplate.exchange(fileInfoUri, HttpMethod.GET, request, String.class, token, fileId);
	}

}
