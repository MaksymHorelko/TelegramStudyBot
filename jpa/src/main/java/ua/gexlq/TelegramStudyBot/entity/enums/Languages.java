package ua.gexlq.TelegramStudyBot.entity.enums;

public enum Languages {
	UKRANIAN("uk"), RUSSIAN("ru"), ENGLISH("en");

	private final String language;

	Languages(String language) {
		this.language = language;
	}

    public static Languages fromValue(String v) {
        for (Languages c : Languages.values()) {
            if (c.language.equals(v)) {
                return c;
            }
        }
        return null;
    }
	
	@Override
	public String toString() {
		return language;
	}

}
