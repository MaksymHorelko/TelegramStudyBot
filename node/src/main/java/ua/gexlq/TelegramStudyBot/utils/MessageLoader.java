package ua.gexlq.TelegramStudyBot.utils;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Component
public class MessageLoader {

	private Map<String, Object> data;

	public void loadData(String language) {
		String fileName = "messages_" + language + ".yaml";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

		if (inputStream == null) {
			throw new IllegalArgumentException("File " + fileName + " not found!");
		}

		Yaml yaml = new Yaml();
		data = yaml.load(inputStream);
	}

	public String getTextByCode(String code, String language) {
		loadData(language);

		String[] keys = code.split("\\.");
		
		String text = getValueFromMap(data, keys, 0);
		
		return text;
	}

	@SuppressWarnings("unchecked")
	private String getValueFromMap(Map<String, Object> currentMap, String[] keys, int index) {
		if (index < keys.length) {
			String key = keys[index];
			
			if (currentMap.containsKey(key)) {
				Object nextLevel = currentMap.get(key);

				if (nextLevel instanceof Map) {
					return getValueFromMap((Map<String, Object>) nextLevel, keys, index + 1);
				} else if (nextLevel instanceof String) {
					return (String) nextLevel;
				}
			}
		}
		return "error";
	}

}
