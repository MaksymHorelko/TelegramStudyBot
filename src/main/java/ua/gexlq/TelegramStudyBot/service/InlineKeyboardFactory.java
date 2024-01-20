package ua.gexlq.TelegramStudyBot.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import ua.gexlq.TelegramStudyBot.handler.Logger;
import ua.gexlq.TelegramStudyBot.handler.MessageHandler;

public class InlineKeyboardFactory {
	private static int maxSemesters = 10;
	private static String nextButtonCallBackData;
	private static String backButtonCallBackData;

	// CALL_BACK_DATA CODES
	private static String setUserFaculty = "[setUserFacultyTo]->";
	private static String setUserSpecialization = "[setUserSpecializationTo]->";
	private static String setUserSemester = "[setUserSemesterTo]->";
	private static String changeUserLanguage = "[changeUserLanguageTo]->";

	private static String selectSubject = "[selectSubject]->";
	private static String selectWorkType = "[selectWorkType]->";
	private static String selectWork = "[selectWork]->";
	private static String selectOption = "[selectOption]->";

	private static String goBackTo = "[goBackTo]->";

	private static String getButtonTextByCode(String code, String language) {
		return MessageHandler.getMessage(code, language);
	}

	private static List<InlineKeyboardButton> createButton(String buttonText, String callbackData, String language) {
		var button = new InlineKeyboardButton();
		button.setText(buttonText);
		button.setCallbackData(callbackData);
		return Collections.singletonList(button);
	}

	private static List<InlineKeyboardButton> createNextButton(String language) {
		return createButton(getButtonTextByCode("menu.next", language), nextButtonCallBackData, language);
	}

	private static List<InlineKeyboardButton> createBackButton(String language) {
		return createButton(getButtonTextByCode("menu.back", language), backButtonCallBackData, language);
	}

	private static List<InlineKeyboardButton> createRow(List<String> names, List<String> callbackData, String language,
			int startIdx, int colsPerRow) {
		List<InlineKeyboardButton> row = new ArrayList<>();

		for (int i = startIdx; i < startIdx + colsPerRow && i < names.size(); i++) {
			row.addAll(createButton(names.get(i), callbackData.get(i), language));
		}

		return row;
	}

	private static InlineKeyboardMarkup createPage(List<String> names, List<String> callbackData, String language,
			boolean nextButton, boolean backButton) {
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

		int colsPerRow = 2;
		if (names.size() >= 15) {
			colsPerRow = 3;
		}
		if (names.size() >= 24) {
			colsPerRow = 4;
		}

		for (int i = 0; i < names.size(); i += colsPerRow) {
			rowsInLine.add(createRow(names, callbackData, language, i, colsPerRow));
		}

		if (backButton)
			rowsInLine.add(createBackButton(language));

		if (nextButton)
			rowsInLine.add(createNextButton(language));

		inlineKeyboardMarkup.setKeyboard(rowsInLine);
		return inlineKeyboardMarkup;
	}

	// CREATE LIST
	private static List<String> createList(String base) {
		List<String> list = new ArrayList<>();
		String code;
		boolean end = false;
		int i = 1;
		while (!end) {
			code = base + "." + i;
			if (MessageHandler.getMessage(code, "uk") == null) {
				end = true;
			} else {
				list.add(code);
				i++;
			}
		}
		
		return list;
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT [FIRST LAUNCH] || MAIN MENU ->
	// SETTINGS MENU -> CHANGE DATA -> CHANGE FACULTY
	public static InlineKeyboardMarkup createFacultyPage(String language) {
		String faculty = "faculty";

		List<String> facultyCallBackData = new ArrayList<>();
		List<String> facultyNames = new ArrayList<>();

		int i = 1;
		for (String code : createList(faculty)) {
			facultyNames.add(getButtonTextByCode(code, language));
			facultyCallBackData.add(setUserFaculty + i);
			i++;
		}

		return createPage(facultyNames, facultyCallBackData, language, false, false);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT [FIRST LAUNCH] || MAIN MENU ->
	// SETTINGS MENU -> CHANGE DATA -> CHANGE SPECIALIZATION
	public static InlineKeyboardMarkup createSpecializationPage(String faculty, String language) {
		String spec = "specialization" + "." + faculty;

		List<String> specializationCallBackData = new ArrayList<>();
		List<String> specializationName = new ArrayList<>();

		for (String code : createList(spec)) {
			specializationName.add(getButtonTextByCode(code, language));
		}

		for (String name : specializationName) {
			specializationCallBackData.add(setUserSpecialization + name);
		}

		return createPage(specializationName, specializationCallBackData, language, false, false);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT [FIRST LAUNCH] || MAIN MENU ->
	// SETTINGS MENU -> CHANGE DATA -> CHANGE SEMESTER
	public static InlineKeyboardMarkup createSemesterPage(String language) {
		List<String> semesterName = new ArrayList<>();
		List<String> semesterCallBackData = new ArrayList<>();

		for (int i = 1; i < maxSemesters + 1; i++)
			semesterName.add(Integer.toString(i));

		for (String semester : semesterName)
			semesterCallBackData.add(setUserSemester + semester);

		return createPage(semesterName, semesterCallBackData, language, false, false);
	}

	// MAIN MENU -> SETTINGS MENU -> CHANGE DATA
	public static InlineKeyboardMarkup createChangeDataPage(String language) {

		List<String> dataCallBackData = Arrays.asList("changeFaculty", "changeSpecialization", "changeSemester");

		List<String> dataName = new ArrayList<>();

		for (String code : Arrays.asList("faculty", "specialization", "semester")) {
			dataName.add(getButtonTextByCode(code, language));
		}

		return createPage(dataName, dataCallBackData, language, false, false);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT
	public static InlineKeyboardMarkup createSubjectPage(String faculty, String specialization, String semester,
			String language) {

		StringBuilder base = new StringBuilder("subject.").append(faculty).append('.').append(specialization)
				.append('.').append(semester);

		List<String> subjectCallBackData = createList(base.toString());

		if (subjectCallBackData == null) {
			return null;
		}

		List<String> subjectNames = new ArrayList<>();

		for (String code : subjectCallBackData) {
			subjectNames.add(getButtonTextByCode(code, language));
		}

		for (int i = 0; i < subjectCallBackData.size(); i++) {
			subjectCallBackData.set(i, subjectCallBackData.get(i).replace("subject.", selectSubject));
		}

		return createPage(subjectNames, subjectCallBackData, language, false, false);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT-> CHOOSE WORKTYPE (TEST, LAB,
	// PRAC..)
	public static InlineKeyboardMarkup createWorkTypePage(String subject, String language) {
		String base = "workType" + ".";
		List<String> codeToCategory = Arrays.asList("courses", "exams", "tests", "modulars", "labs", "pracs");

		List<String> categoryName = new ArrayList<>();
		List<String> categoryCallBackData = new ArrayList<>();

		for (String code : codeToCategory) {
			categoryName.add(getButtonTextByCode(base + code, language));
			categoryCallBackData.add(selectWorkType + subject + "." + code);
		}

		backButtonCallBackData = goBackTo;

		return createPage(categoryName, categoryCallBackData, language, false, true);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT -> CHOOSE WORKTYPE -> CHOOSE WORK
	public static InlineKeyboardMarkup createPickWorkPage(String subject, String workType, String language) {

		List<String> workNames = new ArrayList<>();
		List<String> callBackData = new ArrayList<>();

		String workTypeName = MessageHandler.getMessage("workType" + "." + workType + "." + "abbr", language);

		try {
			int countWorks = Integer
					.parseInt(MessageHandler.getMessage("subject." + subject + "." + workType, language));

			for (int i = 1; i <= countWorks; i++) {
				workNames.add(workTypeName + " " + i);
				callBackData.add(selectWork + subject + "." + workType + "." + i);
			}
		} catch (NumberFormatException e) {
			Logger.logError(e);
			return null;
		}

		if (workNames.isEmpty()) {
			return null;
		}

		backButtonCallBackData = goBackTo + "workTypePage";

		return createPage(workNames, callBackData, language, false, true);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT -> CHOOSE WORKTYPE -> WORK -> CHOOSE
	// OPTION (DOWNLOAD OR
	// UPLOAD)
	public static InlineKeyboardMarkup createWorkOptionPage(String selectedWork, String language) {
		String base = "menu.inline.";
		List<String> options = Arrays.asList("upload", "download");

		List<String> optionCallBackData = new ArrayList<>();

		List<String> optionName = new ArrayList<>();

		for (String option : options) {
			optionName.add(getButtonTextByCode(base + option, language));
		}

		for (String option : options) {
			optionCallBackData.add(selectOption + option + "->" + selectedWork);
		}

		return createPage(optionName, optionCallBackData, language, false, false);
	}

	// MAIN MENU -> SETTINGS MENU -> CHANGE LANGUAGE
	public static InlineKeyboardMarkup createChangeLanguagePage(String language) {
		String base = "language.";
		List<String> languages = Arrays.asList("ukrainian", "russian", "english");

		List<String> languageCallBackData = new ArrayList<>();
		List<String> languageName = new ArrayList<>();

		for (String lang : languages) {
			languageCallBackData.add(changeUserLanguage + lang);
		}

		for (String lang : languages) {
			languageName.add(getButtonTextByCode(base + lang, language));
		}

		return createPage(languageName, languageCallBackData, language, false, false);
	}
}
