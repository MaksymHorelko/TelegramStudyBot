package ua.gexlq.TelegramStudyBot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import ua.gexlq.TelegramStudyBot.handler.MessageHandler;

public class InlineKeyboardFactory {

	private static List<InlineKeyboardButton> createButton(String buttonText, String callbackData, String language) {
		var button = new InlineKeyboardButton();
		button.setText(MessageHandler.getMessage(buttonText, language));
		button.setCallbackData(callbackData);
		return Collections.singletonList(button);
	}

	private static List<InlineKeyboardButton> createNextButton(String language) {
		return createButton("menu.next", "menu.next", language);
	}

	private static List<InlineKeyboardButton> createBackButton(String language) {
		return createButton("menu.back", "menu.back", language);
	}

	private static List<InlineKeyboardButton> createFacultyRow(List<String> facultyNames,
			List<String> facultyCallbackData, String language, int startIdx) {
		List<InlineKeyboardButton> row = new ArrayList<>();

		for (int i = startIdx; i < startIdx + 2 && i < facultyNames.size(); i++) {
			row.addAll(createButton(facultyNames.get(i), facultyCallbackData.get(i), language));
		}

		return row;
	}

	private static InlineKeyboardMarkup createPage(List<String> facultyNames, List<String> facultyCallbackData,
			String language) {
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

		for (int i = 0; i < facultyNames.size(); i += 2) {
			rowsInLine.add(createFacultyRow(facultyNames, facultyCallbackData, language, i));
		}

		inlineKeyboardMarkup.setKeyboard(rowsInLine);
		return inlineKeyboardMarkup;
	}

	public static InlineKeyboardMarkup createFacultyPage(String language) {
		List<String> faculty = Arrays.asList("faculty.biology", "faculty.geo", "faculty.econom", "faculty.languages",
				"faculty.history", "faculty.csd", "faculty.mathematics", "faculty.medicine", "faculty.irtb",
				"faculty.psychology", "faculty.radiophysics", "faculty.sociology", "faculty.physics",
				"faculty.philology", "faculty.philosophy", "faculty.chemistry", "faculty.law");

		return createPage(faculty, faculty, language);
	}

	public static InlineKeyboardMarkup createSemesterPage(String language) {
		List<String> semester = Arrays.asList("semester.1", "semester.2", "semester.3", "semester.4", "semester.5",
				"semester.6", "semester.7", "semester.8", "semester.9", "semester.10");

		return createPage(semester, semester, language);
	}

	public static InlineKeyboardMarkup createSpecializationPage(String language) {
		List<String> specialization = Arrays.asList("specialization.csd.122", "specialization.csd.123",
				"specialization.csd.125", "specialization.csd.151", "specialization.csd.174");

		return createPage(specialization, specialization, language);
	}

	public static InlineKeyboardMarkup createChangeDataPage(String language) {
		List<String> data = Arrays.asList("faculty", "specialization", "semester");

		return createPage(data, data, language);
	}

	public static InlineKeyboardMarkup createSubjectPage(String faculty, String specialization, String semester,
			String language) {

		List<String> subjects = new ArrayList<>();
		boolean end = false;
		int i = 1;
		
		while (!end) {
			String code = "subject" + "." + faculty + "." + specialization + "." + semester + "." + i;

			if (MessageHandler.getMessage(code, language) == null) {
				end = true;
			} else {
				subjects.add(code);
				i++;
			}
		}
		if(subjects.isEmpty())
			return null;
		
		return createPage(subjects, subjects, language);
	}

	public static InlineKeyboardMarkup createWorkOptionPage(String language) {
		List<String> options = Arrays.asList("menu.works.upload", "menu.works.download");
		
		return createPage(options, options, language);
	}
	
	public static InlineKeyboardMarkup createChangeLanguagePage(String language) {
		List<String> data = Arrays.asList("ukranian", "russian", "english");

		return createPage(data, data, language);
	}

}
