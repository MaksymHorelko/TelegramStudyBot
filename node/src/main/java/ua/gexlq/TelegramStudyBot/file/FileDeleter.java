package ua.gexlq.TelegramStudyBot.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

@Component
public class FileDeleter {

	public void deleteFileFromSystem(String filePath) throws IOException {
		try {
			Path path = Paths.get(filePath);
			Files.delete(path);
		} catch (IOException e) {
			throw new IOException("file not found");
		}
	}
}
