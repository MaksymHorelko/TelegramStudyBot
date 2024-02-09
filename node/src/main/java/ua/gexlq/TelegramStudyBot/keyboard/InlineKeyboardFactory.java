package ua.gexlq.TelegramStudyBot.keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.gexlq.TelegramStudyBot.process.callBackData.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class InlineKeyboardFactory {

	private final MessageUtils messageUtils;

	private final int maxSemesters = 10;
	private String nextButtonCallBackData;
	private String backButtonCallBackData;

	private final String POINTER = "->";

	private String getButtonTextByCode(String code, String language) {
		return messageUtils.getAnswerTextByCode(code, language);
	}

	private List<InlineKeyboardButton> createButton(String buttonText, String callbackData, String language) {
		var button = new InlineKeyboardButton();
		button.setText(buttonText);
		button.setCallbackData(callbackData);
		return Collections.singletonList(button);
	}

	private List<InlineKeyboardButton> createNextButton(String language) {
		return createButton(getButtonTextByCode("menu.next", language), nextButtonCallBackData, language);
	}

	private List<InlineKeyboardButton> createBackButton(String language) {
		return createButton(getButtonTextByCode("menu.back", language), backButtonCallBackData, language);
	}

	private List<InlineKeyboardButton> createRow(List<String> names, List<String> callbackData, String language,
			int startIdx, int colsPerRow) {
		List<InlineKeyboardButton> row = new ArrayList<>();

		for (int i = startIdx; i < startIdx + colsPerRow && i < names.size(); i++) {
			row.addAll(createButton(names.get(i), callbackData.get(i), language));
		}

		return row;
	}

	private InlineKeyboardMarkup createPage(List<String> names, List<String> callbackData, String language,
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
	private List<String> createList(String base) {
		List<String> list = new ArrayList<>();
		String code;
		boolean end = false;
		int i = 1;
		while (!end) {
			code = base + "." + i;
			if (messageUtils.getAnswerTextByCode(code, "uk").equals("error")) {
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
	public InlineKeyboardMarkup createFacultyPage(String language) {
		String faculty = "faculty";

		List<String> facultyCallBackData = new ArrayList<>();
		List<String> facultyNames = new ArrayList<>();

		int i = 1;
		for (String code : createList(faculty)) {
			facultyNames.add(getButtonTextByCode(code, language));
			facultyCallBackData.add(CallBackDataTypes.SET_USER_FACULTY_TO + POINTER + i);
			i++;
		}

		return createPage(facultyNames, facultyCallBackData, language, false, false);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT [FIRST LAUNCH] || MAIN MENU ->
	// SETTINGS MENU -> CHANGE DATA -> CHANGE SPECIALIZATION
	public InlineKeyboardMarkup createSpecializationPage(String faculty, String language) {
		String spec = "specialization" + "." + faculty;

		List<String> specializationCallBackData = new ArrayList<>();
		List<String> specializationName = new ArrayList<>();

		for (String code : createList(spec)) {
			specializationName.add(getButtonTextByCode(code, language));
		}

		for (String name : specializationName) {
			specializationCallBackData.add(CallBackDataTypes.SET_USER_SPECIALIZATION_TO + POINTER + name);
		}

		return createPage(specializationName, specializationCallBackData, language, false, false);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT [FIRST LAUNCH] || MAIN MENU ->
	// SETTINGS MENU -> CHANGE DATA -> CHANGE SEMESTER
	public InlineKeyboardMarkup createSemesterPage(String language) {
		List<String> semesterName = new ArrayList<>();
		List<String> semesterCallBackData = new ArrayList<>();

		for (int i = 1; i < maxSemesters + 1; i++)
			semesterName.add(Integer.toString(i));

		for (String semester : semesterName)
			semesterCallBackData.add(CallBackDataTypes.SET_USER_SEMESTER_TO + POINTER + semester);

		return createPage(semesterName, semesterCallBackData, language, false, false);
	}

	// MAIN MENU -> SETTINGS MENU -> CHANGE DATA
	public InlineKeyboardMarkup createChangeDataPage(String language) {

		List<String> dataCallBackData = Arrays.asList(CallBackDataTypes.CHANGE_FACULTY.toString(),
				CallBackDataTypes.CHANGE_SPECIALIZATION.toString(), CallBackDataTypes.CHANGE_SEMESTER.toString());

		List<String> dataName = new ArrayList<>();

		for (String code : Arrays.asList("faculty.name", "specialization.name", "semester.name")) {
			dataName.add(getButtonTextByCode(code, language));
		}

		return createPage(dataName, dataCallBackData, language, false, false);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT
	public InlineKeyboardMarkup createSubjectPage(String faculty, String specialization, String semester,
			String language) {

		StringBuilder base = new StringBuilder("subjects.").append(faculty).append('.').append(specialization)
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
			subjectCallBackData.set(i, subjectCallBackData.get(i).replace("subjects.",
					CallBackDataTypes.SELECT_SUBJECT.toString() + POINTER));
		}

		return createPage(subjectNames, subjectCallBackData, language, false, false);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT-> CHOOSE WORKTYPE (TEST, LAB,
	// PRAC..)
	public InlineKeyboardMarkup createWorkTypePage(String subject, String language) {
		String base = "workTypes" + ".";
		List<String> codeToCategory = Arrays.asList("1", "2", "3", "4", "5", "6");

		List<String> categoryName = new ArrayList<>();
		List<String> categoryCallBackData = new ArrayList<>();

		for (String code : codeToCategory) {
			categoryName.add(getButtonTextByCode(base + code + ".name", language));
			categoryCallBackData.add(CallBackDataTypes.SELECT_WORK_TYPE + POINTER + subject + "." + code);
		}

		backButtonCallBackData = CallBackDataTypes.GO_BACK_TO.toString() + POINTER
				+ CallBackDataTypes.SELECT_SUBJECT_PAGE.toString();

		return createPage(categoryName, categoryCallBackData, language, false, true);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT -> CHOOSE WORKTYPE -> CHOOSE WORK
	public InlineKeyboardMarkup createPickWorkPage(String subject, String workType, String language) {

		List<String> workNames = new ArrayList<>();
		List<String> callBackData = new ArrayList<>();

		String workTypeName = messageUtils.getAnswerTextByCode("workTypes" + "." + workType + "." + "abbr", language);

		try {
			int countWorks = Integer
					.parseInt(messageUtils.getAnswerTextByCode("works." + subject + "." + workType, language));

			for (int i = 1; i <= countWorks; i++) {
				workNames.add(workTypeName + " " + i);
				callBackData.add(CallBackDataTypes.SELECT_WORK + POINTER + subject + "." + workType + "." + i);
			}
		} catch (NumberFormatException e) {
			log.error("INVALID NUMBER");
			return null;
		}

		if (workNames.isEmpty()) {
			return null;
		}

		backButtonCallBackData = CallBackDataTypes.GO_BACK_TO + POINTER + CallBackDataTypes.SELECT_SUBJECT + POINTER
				+ subject;

		return createPage(workNames, callBackData, language, false, true);
	}

	// MAIN MENU -> WORK MENU -> CHOOSE SUBJECT -> CHOOSE WORKTYPE -> WORK -> CHOOSE
	// OPTION (DOWNLOAD OR
	// UPLOAD)
	public InlineKeyboardMarkup createWorkOptionPage(String selectedWork, String language) {
		String upload = "menu.inline.upload";
		String download = "menu.inline.download";

		List<String> options = Arrays.asList(CallBackDataTypes.UPLOAD.toString(),
				CallBackDataTypes.DOWNLOAD.toString());

		List<String> optionCallBackData = new ArrayList<>();

		List<String> optionName = new ArrayList<>();

		optionName.add(getButtonTextByCode(upload, language));
		optionName.add(getButtonTextByCode(download, language));

		for (String option : options) {
			optionCallBackData.add(option + POINTER + selectedWork);
		}

		backButtonCallBackData = CallBackDataTypes.GO_BACK_TO.toString() + POINTER
				+ CallBackDataTypes.SELECT_WORK_TYPE.toString() + POINTER
				+ selectedWork.substring(0, selectedWork.lastIndexOf("."));

		return createPage(optionName, optionCallBackData, language, false, true);
	}

	public InlineKeyboardMarkup createUDPage(String selectedWork, String language) {

		backButtonCallBackData = CallBackDataTypes.GO_BACK_TO.toString() + POINTER
				+ CallBackDataTypes.SELECT_WORK_TYPE.toString()
				+ selectedWork.substring(0, selectedWork.lastIndexOf("."));

		return createPage(new ArrayList<String>(), new ArrayList<String>(), language, false, true);
	}

	public InlineKeyboardMarkup createRateContentPage(String language, String selectedWork) {

		List<String> rateName = new ArrayList<>();
		List<String> rateCallBackData = new ArrayList<>();

		for (int i = 10; i < 110; i += 10)
			rateName.add(Integer.toString(i) + "%");

		for (String rate : rateName)
			rateCallBackData.add(CallBackDataTypes.RATE_CONTENT + POINTER + rate);

		backButtonCallBackData = CallBackDataTypes.GO_BACK_TO.toString() + POINTER + CallBackDataTypes.SELECT_WORK
				+ POINTER + selectedWork;

		return createPage(rateName, rateCallBackData, language, false, true);
	}

	public InlineKeyboardMarkup createRateImplementationPage() {
		List<String> rateName = new ArrayList<>();
		List<String> rateCallBackData = new ArrayList<>();

		for (int i = 10; i < 110; i += 10)
			rateName.add(Integer.toString(i) + "%");

		for (String rate : rateName)
			rateCallBackData.add(CallBackDataTypes.RATE_IMPLEMENTATION + POINTER + rate);

		return createPage(rateName, rateCallBackData, "uk", false, false);
	}

	public InlineKeyboardMarkup createRateMarkPage() {

		List<String> rateName = new ArrayList<>();
		List<String> rateCallBackData = new ArrayList<>();

		for (int i = 10; i < 110; i += 10)
			rateName.add(Integer.toString(i) + "%");

		for (String rate : rateName)
			rateCallBackData.add(CallBackDataTypes.RATE_MARK + POINTER + rate);

		return createPage(rateName, rateCallBackData, "uk", false, false);
	}

	public InlineKeyboardMarkup createRatedCorrectionPage(String language, String workCode) {

		List<String> rateName = new ArrayList<>();
		List<String> rateCallBackData = new ArrayList<>();

		nextButtonCallBackData = CallBackDataTypes.GO_NEXT_TO.toString() + POINTER + CallBackDataTypes.UPLOAD_FILE;
		backButtonCallBackData = CallBackDataTypes.GO_BACK_TO.toString() + POINTER + CallBackDataTypes.UPLOAD + POINTER
				+ workCode;
		return createPage(rateName, rateCallBackData, language, true, true);
	}

	public InlineKeyboardMarkup createUploadRulesPage(String language) {

		List<String> getRulesName = new ArrayList<>();
		List<String> getRulesCallBackData = new ArrayList<>();

		getRulesName.add(getButtonTextByCode("menu.inline.moreInfo", language));
		getRulesCallBackData.add(CallBackDataTypes.UPLOAD_RULES.toString() + POINTER);

		return createPage(getRulesName, getRulesCallBackData, language, false, false);
	}

	// MAIN MENU -> SETTINGS MENU -> CHANGE LANGUAGE
	public InlineKeyboardMarkup createChangeLanguagePage(String language) {
		String base = "languages" + ".";
		List<String> languages = Arrays.asList("ukrainian", "russian", "english");

		List<String> languageCallBackData = new ArrayList<>();
		List<String> languageName = new ArrayList<>();

		for (String lang : languages) {
			languageCallBackData.add(CallBackDataTypes.CHANGE_USER_LANGUAGE_TO + POINTER + lang);
		}

		for (String lang : languages) {
			languageName.add(getButtonTextByCode(base + lang + ".name", language));
		}

		return createPage(languageName, languageCallBackData, language, false, false);
	}
}
