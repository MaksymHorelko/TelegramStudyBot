package ua.gexlq.TelegramStudyBot.service.enums;

import ua.gexlq.TelegramStudyBot.exceptions.UnsupportedMimeTypeException;

public enum SupportedMimeType {
	DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"), PDF("application/pdf"),
	ZIP("application/zip");

	private final String type;

	SupportedMimeType(String type) {
		this.type = type;
	}

	public static String mimeTypeToExtension(String mimeType) {
		for (SupportedMimeType c : SupportedMimeType.values()) {
			if (c.type.equals(mimeType)) {
				return c.name().toLowerCase();
			}
		}

		throw new UnsupportedMimeTypeException("The mime type: " + mimeType + " is not supported");
	}

	public static boolean isSupportedMimeType(String v) {
		for (SupportedMimeType c : SupportedMimeType.values()) {
			if (c.type.equals(v)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return type;
	}

}
