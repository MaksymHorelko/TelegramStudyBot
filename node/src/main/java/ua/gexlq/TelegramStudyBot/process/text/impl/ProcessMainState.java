package ua.gexlq.TelegramStudyBot.process.text.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.entity.AppUser;
import ua.gexlq.TelegramStudyBot.entity.enums.UserState;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.FacultyPage;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.SemesterPage;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.SoftwarePage;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.SpecializationPage;
import ua.gexlq.TelegramStudyBot.keyboard.inline.pages.SubjectPage;
import ua.gexlq.TelegramStudyBot.keyboard.menu.menus.HelpMenu;
import ua.gexlq.TelegramStudyBot.keyboard.menu.menus.MainMenu;
import ua.gexlq.TelegramStudyBot.keyboard.menu.menus.SettingsMenu;
import ua.gexlq.TelegramStudyBot.process.text.ProcessMessageByUserState;
import ua.gexlq.TelegramStudyBot.process.text.enums.MainCommand;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ProcessMainState implements ProcessMessageByUserState {

	private final AppUserDAO appUserDAO;
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;

	private Map<String, MainCommand> commandToEnumMapping = new HashMap<>();

	private final MainMenu mainMenu;
	private final HelpMenu helpMenu;
	private final SettingsMenu settingsMenu;

	private final FacultyPage facultyPage;
	private final SpecializationPage specializationPage;
	private final SemesterPage semesterPage;
	private final SubjectPage subjectPage;
	private final SoftwarePage softwarePage;

	private final String START_COMMAND = "/start";
	private final String MENU_WORKS = "menu.works.name";
	private final String MENU_SOFTWARE = "menu.software.name";
	private final String MENU_HELP = "menu.help.name";
	private final String MENU_SETTINGS = "menu.settings.name";

	private final String MESSAGE_WELCOME = "message.welcome.name";
	private final String MESSAGE_SOFTWARE = "message.software.name";
	private final String MESSAGE_HELP = "message.help.name";
	private final String MESSAGE_SETTINGS = "message.settings.name";
	private final String MESSAGE_UNKNOWN_COMMAND = "message.unknownCommand";

	private final String MESSAGE_PICK_SUBJECT = "message.pick.subject";
	private final String MESSAGE_PICK_FACULTY = "message.pick.faculty";
	private final String MESSAGE_PICK_SPECIALIZATION = "message.pick.specialization";
	private final String MESSAGE_PICK_SEMESTER = "message.pick.semester";

	@Override
	public SendMessage handle(Update update) {
		long chatId = update.getMessage().getChatId();
		String messageText = update.getMessage().getText();
		String language = userInfo.getUserLanguage(chatId);

		initMap(language);

		SendMessage response;

		MainCommand mainCommand = getMainCommand(messageText);

		if (mainCommand != null) {
			response = handleCommand(update, mainCommand, language);
		} else {
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UNKNOWN_COMMAND);
			response.setReplyMarkup(mainMenu.createMainMenuKeyboard(language));
		}

		return response;
	}

	private SendMessage handleCommand(Update update, MainCommand mainCommand, String language) {
		SendMessage response;
		long chatId = update.getMessage().getChatId();

		switch (mainCommand) {

		case START:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_WELCOME);
			response.setReplyMarkup(mainMenu.createMainMenuKeyboard(language));
			break;

		case WORKS:
			if (userInfo.isUserFacultySet(chatId)) {
				response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_PICK_FACULTY);
				response.setReplyMarkup(facultyPage.createFacultyPage(language));
			}

			else if (userInfo.isUserSpecializationSet(chatId)) {
				String faculty = userInfo.getUserFaculty(chatId);
				response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_PICK_SPECIALIZATION);
				response.setReplyMarkup(specializationPage.createSpecializationPage(faculty, language));
			}

			else if (userInfo.isUserSemesterSet(chatId)) {
				response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_PICK_SEMESTER);
				response.setReplyMarkup(semesterPage.createSemesterPage());
			}

			else {
				response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_PICK_SUBJECT);

				response.setReplyMarkup(subjectPage.createSubjectPage(userInfo.getUserFaculty(chatId),
						userInfo.getUserSpecialization(chatId), userInfo.getUserSemester(chatId), language));

				var user = appUserDAO.findUserByTelegramUserId(update.getMessage().getFrom().getId());
				user.setCurrentActiveMessageId(String.valueOf(update.getMessage().getMessageId() + 1));
				appUserDAO.save(user);
			}

			break;

		case SOFTWARE:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_SOFTWARE);
			response.setReplyMarkup(softwarePage.createSoftwarePage());
			
			var user = appUserDAO.findUserByTelegramUserId(update.getMessage().getFrom().getId());
			user.setCurrentActiveMessageId(String.valueOf(update.getMessage().getMessageId() + 1));
			appUserDAO.save(user);
			break;

		case HELP:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_HELP);
			response.setReplyMarkup(helpMenu.createHelpMenuKeyboard(language));
			setNewState(update, UserState.HELP_STATE);
			break;

		case SETTINGS:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_SETTINGS);
			response.setReplyMarkup(settingsMenu.createSettingsMenuKeyboard(userInfo.isUserDataSet(chatId), language));
			setNewState(update, UserState.SETTINGS_STATE);
			break;

		default:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UNKNOWN_COMMAND);
			response.setReplyMarkup(mainMenu.createMainMenuKeyboard(language));
		}

		return response;
	}

	private void initMap(String language) {
		commandToEnumMapping.put(START_COMMAND, MainCommand.START);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_WORKS, language), MainCommand.WORKS);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_SOFTWARE, language), MainCommand.SOFTWARE);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_HELP, language), MainCommand.HELP);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_SETTINGS, language), MainCommand.SETTINGS);
	}

	private MainCommand getMainCommand(String messageText) {
		return commandToEnumMapping.get(messageText);
	}

	private void setNewState(Update update, UserState state) {
		AppUser user = appUserDAO.findUserByTelegramUserId(update.getMessage().getChatId());
		user.setUserState(state);
		appUserDAO.save(user);
	}

	@Override
	public UserState getSupportedState() {
		return UserState.MAIN_STATE;
	}

}