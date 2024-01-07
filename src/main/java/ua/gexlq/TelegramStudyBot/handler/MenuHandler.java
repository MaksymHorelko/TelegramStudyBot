package ua.gexlq.TelegramStudyBot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

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

	private long getUserId(Update update) {
		return update.getMessage().getChatId();
	}

	private String getUserMessage(Update update) {
		return update.getMessage().getText();
	}

	private SendMessage setMessage(long chatId) {
		SendMessage message = new SendMessage();
		message.setChatId(String.valueOf(chatId));
		return message;
	}

	private SendMessage pickSubject(long chatId, SendMessage message, String language) {
		message.setText(MessageHandler.getMessage("message.works.subject", language));

		message.setReplyMarkup(InlineKeyboardFactory.createSubjectPage(service.getUserFaculty(chatId),
				service.getUserSpecialization(chatId), service.getUserSemester(chatId), language));

		return message;
	}

	public SendMessage mainMenu(Update update, String language) {

		long chatId = getUserId(update);
		String input = getUserMessage(update);

		SendMessage message = setMessage(chatId);

		if (input.equals("/start")) {
			message.setText(MessageHandler.getMessage("message.welcome", language));
			message.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(language));
		}

		else if (input.equals(MessageHandler.getMessage("menu.works", language))) {
			message.setText(MessageHandler.getMessage("message.works", language));
			message.setReplyMarkup(KeyboardFactory.createWorkMenuKeyboard(language));
			service.setUserState(chatId, UserState.WORK_MENU);
		}

		else if (input.equals(MessageHandler.getMessage("menu.materials", language))) {
			message.setText(MessageHandler.getMessage("message.materials", language));
			message.setReplyMarkup(KeyboardFactory.createMaterialsMenuKeyboard(language));
			service.setUserState(chatId, UserState.MATERIALS_MENU);
		}

		else if (input.equals(MessageHandler.getMessage("menu.help", language))) {
			message.setText(MessageHandler.getMessage("message.help", language));
			message.setReplyMarkup(KeyboardFactory.createHelpMenuKeyboard(language));
			service.setUserState(chatId, UserState.HELP_MENU);
		}

		else if (input.equals(MessageHandler.getMessage("menu.settings", language))) {
			message.setText(MessageHandler.getMessage("message.settings", language));
			message.setReplyMarkup(KeyboardFactory.createSettingsMenuKeyboard(language));
			service.setUserState(chatId, UserState.SETTINGS_MENU);
		}

		else {
			message.setText(MessageHandler.getMessage("message.unknownCommand", language));
			message.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(language));
		}

		return message;
	}

	public SendMessage helpMenu(Update update, String language) {
		long chatId = getUserId(update);
		String input = getUserMessage(update);

		SendMessage message = setMessage(chatId);

		if (input.equals(MessageHandler.getMessage("menu.help.email", language))) {
			message.setText(MessageHandler.getMessage("message.help.email", language).replace("{0}",
					MessageHandler.getMessage("email", language)));
		}

		else if (input.equals(MessageHandler.getMessage("menu.help.chat", language))) {
			message.setText(MessageHandler.getMessage("message.help.chat", language).replace("{0}",
					MessageHandler.getMessage("chatURL", language)));
		}

		else if (input.equals(MessageHandler.getMessage("menu.help.donate", language))) {
			message.setText(MessageHandler.getMessage("message.help.donate", language));
		}

		else if (input.equals(MessageHandler.getMessage("menu.help.commands", language))) {
			message.setText(MessageHandler.getMessage("message.help.commands", language));
		}

		else if (input.equals(MessageHandler.getMessage("menu.back", language))) {
			message.setText(MessageHandler.getMessage("message.stepBackToMainMenu", language));
			message.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(language));
			service.setUserState(chatId, UserState.MAIN_MENU);
		}

		else {
			message.setText(MessageHandler.getMessage("message.unknownCommand", language));
			message.setReplyMarkup(KeyboardFactory.createHelpMenuKeyboard(language));
		}

		return message;
	}

	public SendMessage workMenu(Update update, String language) {
		long chatId = getUserId(update);
		String input = getUserMessage(update);

		SendMessage message = setMessage(chatId);

		if (input.equals(MessageHandler.getMessage("menu.works.subject", language))) {

			if (service.isUserFacultySet(chatId)) {

				message.setText(MessageHandler.getMessage("message.work.subject.faculty", language));
				message.setReplyMarkup(InlineKeyboardFactory.createFacultyPage(language));

			}

			else if (service.isUserSpecializationSet(chatId)) {
				message.setText(MessageHandler.getMessage("message.work.subject.specialization", language));
				message.setReplyMarkup(InlineKeyboardFactory.createSpecializationPage(language));
			}

			else if (service.isUserSemesterSet(chatId)) {
				message.setText(MessageHandler.getMessage("message.work.subject.semester", language));
				message.setReplyMarkup(InlineKeyboardFactory.createSemesterPage(language));
			}

			else {
				message = pickSubject(chatId, message, language);
			}

		}

		else if (input.equals(MessageHandler.getMessage("menu.works.upload", language))) {
			message.setText(MessageHandler.getMessage("message.works.upload", language));
		}

		else if (input.equals(MessageHandler.getMessage("menu.works.view", language))) {
			message.setText(MessageHandler.getMessage("message.works.view", language));
		}

		else if (input.equals(MessageHandler.getMessage("menu.back", language))) {
			message.setText(MessageHandler.getMessage("message.stepBackToMainMenu", language));
			message.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(language));
			service.setUserState(chatId, UserState.MAIN_MENU);
		}

		else {
			message.setText(MessageHandler.getMessage("message.unknownCommand", language));
		}

		return message;
	}

	public SendMessage settingsMenu(Update update, String language) {
		long chatId = getUserId(update);
		String input = getUserMessage(update);

		SendMessage message = setMessage(chatId);

		if (input.equals(MessageHandler.getMessage("menu.settings.language", language))) {
			message.setText(MessageHandler.getMessage("message.settings.language", language));
			message.setReplyMarkup(InlineKeyboardFactory.createChangeLanguagePage(language));

		}

		else if (input.equals(MessageHandler.getMessage("menu.settings.notifications", language))) {
			message.setText(MessageHandler.getMessage("message.settings.notifications", language));
		}

		else if (input.equals(MessageHandler.getMessage("menu.settings.data", language))) {
			message.setText(MessageHandler.getMessage("message.settings.data", language)
					.replace("{0}", service.getUserFaculty(chatId))
					.replace("{1}", service.getUserSpecialization(chatId))
					.replace("{2}", service.getUserSemester(chatId)));
			message.setReplyMarkup(InlineKeyboardFactory.createChangeDataPage(language));
		}

		else if (input.equals(MessageHandler.getMessage("menu.back", language))) {
			message.setText(MessageHandler.getMessage("message.stepBackToMainMenu", language));
			message.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(language));
			service.setUserState(chatId, UserState.MAIN_MENU);
		}

		else {
			message.setText(MessageHandler.getMessage("message.unknownCommand", language));
			message.setReplyMarkup(KeyboardFactory.createSettingsMenuKeyboard(language));
		}

		return message;
	}

	public SendMessage materialsMenu(Update update, String language) {
		long chatId = getUserId(update);
		String input = getUserMessage(update);

		SendMessage message = setMessage(chatId);

		if (input.equals(MessageHandler.getMessage("menu.materials.lectures", language))) {
			message.setText(MessageHandler.getMessage("message.materials.lectures", language));

		}

		else if (input.equals(MessageHandler.getMessage("menu.materials.semester", language))) {
			message = pickSubject(chatId, message, language);

		}

		else if (input.equals(MessageHandler.getMessage("menu.materials.literature", language))) {
			message.setText(MessageHandler.getMessage("message.materials.literature", language));

		}

		else if (input.equals(MessageHandler.getMessage("menu.back", language))) {
			message.setText(MessageHandler.getMessage("message.stepBackToMainMenu", language));
			message.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard(language));
			service.setUserState(chatId, UserState.MAIN_MENU);
		}

		else {
			message.setText(MessageHandler.getMessage("message.unknownCommand", language));
			message.setReplyMarkup(KeyboardFactory.createMaterialsMenuKeyboard(language));
		}

		return message;
	}

}
