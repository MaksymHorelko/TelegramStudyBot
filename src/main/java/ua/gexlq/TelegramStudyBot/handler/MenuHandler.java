package ua.gexlq.TelegramStudyBot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import ua.gexlq.TelegramStudyBot.model.UserService;
import ua.gexlq.TelegramStudyBot.model.UserService.UserState;
import ua.gexlq.TelegramStudyBot.service.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.service.KeyboardFactory;

@Component
public class MenuHandler {

	private UserService service;

	public MenuHandler(UserService service) {
		this.service = service;
	}

	private long getUserId(Message message) {
		return message.getChatId();
	}

	private String getUserMessage(Message message) {
		return message.getText();
	}

	private SendMessage setMessage(long chatId) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(String.valueOf(chatId));
		return sendMessage;
	}

	private SendMessage pickSubject(long chatId, SendMessage sendMessage, String language) {
		sendMessage.setText(MessageHandler.getMessage("message.works.subject", language));

		sendMessage.setReplyMarkup(InlineKeyboardFactory.createSubjectPage(service.getUserFaculty(chatId),
				service.getUserSpecialization(chatId), service.getUserSemester(chatId), language));

		return sendMessage;
	}

	public SendMessage mainMenu(Message message, String language) {

		long chatId = getUserId(message);
		String input = getUserMessage(message);

		SendMessage sendMessage = setMessage(chatId);

		if (input.equals("/start")) {
			sendMessage.setText(MessageHandler.getMessage("message.welcome", language));
			sendMessage.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(language));
		}

		else if (input.equals(MessageHandler.getMessage("menu.works", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.works", language));
			sendMessage.setReplyMarkup(KeyboardFactory.createWorkMenuKeyboard(language));
			service.setUserState(chatId, UserState.WORK_MENU);
		}

		else if (input.equals(MessageHandler.getMessage("menu.materials", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.materials", language));
			sendMessage.setReplyMarkup(KeyboardFactory.createMaterialsMenuKeyboard(language));
			service.setUserState(chatId, UserState.MATERIALS_MENU);
		}

		else if (input.equals(MessageHandler.getMessage("menu.help", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.help", language));
			sendMessage.setReplyMarkup(KeyboardFactory.createHelpMenuKeyboard(language));
			service.setUserState(chatId, UserState.HELP_MENU);
		}

		else if (input.equals(MessageHandler.getMessage("menu.settings", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.settings", language));
			sendMessage.setReplyMarkup(KeyboardFactory.createSettingsMenuKeyboard(service.isUserEmpty(chatId), language));
			service.setUserState(chatId, UserState.SETTINGS_MENU);
		}

		else {
			sendMessage.setText(MessageHandler.getMessage("message.unknownCommand", language));
			sendMessage.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(language));
		}

		return sendMessage;
	}

	public SendMessage helpMenu(Message message, String language) {
		long chatId = getUserId(message);
		String input = getUserMessage(message);

		SendMessage sendMessage = setMessage(chatId);

		if (input.equals(MessageHandler.getMessage("menu.help.email", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.help.email", language).replace("{0}",
					MessageHandler.getMessage("email", language)));
		}

		else if (input.equals(MessageHandler.getMessage("menu.help.chat", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.help.chat", language).replace("{0}",
					MessageHandler.getMessage("chatURL", language)));
		}

		else if (input.equals(MessageHandler.getMessage("menu.help.donate", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.help.donate", language));
		}

		else if (input.equals(MessageHandler.getMessage("menu.help.commands", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.help.commands", language));
		}

		else if (input.equals(MessageHandler.getMessage("menu.back", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.stepBackToMainMenu", language));
			sendMessage.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(language));
			service.setUserState(chatId, UserState.MAIN_MENU);
		}

		else {
			sendMessage.setText(MessageHandler.getMessage("message.unknownCommand", language));
			sendMessage.setReplyMarkup(KeyboardFactory.createHelpMenuKeyboard(language));
		}

		return sendMessage;
	}

	public SendMessage workMenu(Message message, String language) {
		long chatId = getUserId(message);
		String input = getUserMessage(message);

		SendMessage sendMessage = setMessage(chatId);

		if (input.equals(MessageHandler.getMessage("menu.works.subject", language))) {

			if (!service.isUserFacultySet(chatId)) {
				sendMessage.setText(MessageHandler.getMessage("message.pickFaculty", language));
				sendMessage.setReplyMarkup(InlineKeyboardFactory.createFacultyPage(language));
			}

			else if (!service.isUserSpecializationSet(chatId)) {
				String faculty = service.getUserFaculty(chatId);
				sendMessage.setText(MessageHandler.getMessage("message.pickSpecialization", language));
				sendMessage.setReplyMarkup(InlineKeyboardFactory.createSpecializationPage(faculty, language));
			}

			else if (!service.isUserSemesterSet(chatId)) {
				sendMessage.setText(MessageHandler.getMessage("message.pickSemester", language));
				sendMessage.setReplyMarkup(InlineKeyboardFactory.createSemesterPage(language));
			}

			else {
				sendMessage = pickSubject(chatId, sendMessage, language);
			}

		}

		else if (input.equals(MessageHandler.getMessage("menu.works.view", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.works.viewMyUploadedWorks", language));
		}

		else if (input.equals(MessageHandler.getMessage("menu.back", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.stepBackToMainMenu", language));
			sendMessage.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(language));
			service.setUserState(chatId, UserState.MAIN_MENU);
		}

		else {
			sendMessage.setText(MessageHandler.getMessage("message.unknownCommand", language));
		}

		return sendMessage;
	}

	public SendMessage settingsMenu(Message message, String language) {
		long chatId = getUserId(message);
		String input = getUserMessage(message);

		SendMessage sendMessage = setMessage(chatId);

		if (input.equals(MessageHandler.getMessage("menu.settings.language", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.settings.language", language));
			sendMessage.setReplyMarkup(InlineKeyboardFactory.createChangeLanguagePage(language));

		}

		else if (input.equals(MessageHandler.getMessage("menu.settings.notifications", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.settings.notifications", language));
		}

		else if (input.equals(MessageHandler.getMessage("menu.settings.data", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.settings.data", language)
					.replace("{0}",
							MessageHandler.getMessage("faculty" + "." + service.getUserFaculty(chatId), language))
					.replace("{1}", service.getUserSpecialization(chatId))
					.replace("{2}", service.getUserSemester(chatId)));
			sendMessage.setReplyMarkup(InlineKeyboardFactory.createChangeDataPage(language));
		}

		else if (input.equals(MessageHandler.getMessage("menu.back", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.stepBackToMainMenu", language));
			sendMessage.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(language));
			service.setUserState(chatId, UserState.MAIN_MENU);
		}

		else {
			sendMessage.setText(MessageHandler.getMessage("message.unknownCommand", language));
			sendMessage.setReplyMarkup(KeyboardFactory.createSettingsMenuKeyboard(service.isUserEmpty(chatId), language));
		}

		return sendMessage;
	}

	public SendMessage materialsMenu(Message message, String language) {
		long chatId = getUserId(message);
		String input = getUserMessage(message);

		SendMessage sendMessage = setMessage(chatId);

		if (input.equals(MessageHandler.getMessage("menu.materials.lectures", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.materials.lectures", language));

		}

		else if (input.equals(MessageHandler.getMessage("menu.materials.semester", language))) {
			sendMessage = pickSubject(chatId, sendMessage, language);

		}

		else if (input.equals(MessageHandler.getMessage("menu.materials.literature", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.materials.literature", language));

		}

		else if (input.equals(MessageHandler.getMessage("menu.back", language))) {
			sendMessage.setText(MessageHandler.getMessage("message.stepBackToMainMenu", language));
			sendMessage.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(language));
			service.setUserState(chatId, UserState.MAIN_MENU);
		}

		else {
			sendMessage.setText(MessageHandler.getMessage("message.unknownCommand", language));
			sendMessage.setReplyMarkup(KeyboardFactory.createMaterialsMenuKeyboard(language));
		}

		return sendMessage;
	}

}
