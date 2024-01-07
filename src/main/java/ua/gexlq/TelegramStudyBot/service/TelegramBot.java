package ua.gexlq.TelegramStudyBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ua.gexlq.TelegramStudyBot.config.Config;
import ua.gexlq.TelegramStudyBot.handler.MessageHandler;
import ua.gexlq.TelegramStudyBot.handler.Logger;
import ua.gexlq.TelegramStudyBot.handler.MenuHandler;
import ua.gexlq.TelegramStudyBot.model.UserService;
import ua.gexlq.TelegramStudyBot.model.UserService.UserState;

@Component
public class TelegramBot extends TelegramLongPollingBot {

	@Autowired
	private UserService service;

	@Autowired
	private MenuHandler menuHandler;

	private final Config config;

	public TelegramBot(Config config) {
		this.config = config;
	}

	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {

			long chatId = update.getMessage().getChatId();

			if (update.getMessage().getText().equals("/start") && service.isUserEmpty(chatId)) {
				firstLaunch(update);
			}

			UserState userState = service.getUserState(chatId);
			String language = service.getUserLanguage(chatId);

			switch (userState) {

			case MAIN_MENU:
				send(menuHandler.mainMenu(update, language));
				break;

			case WORK_MENU:
				send(menuHandler.workMenu(update, language));
				break;

			case HELP_MENU:
				send(menuHandler.helpMenu(update, language));
				break;

			case SETTINGS_MENU:
				SendMessage message = menuHandler.settingsMenu(update, language);

				if (message.getText().equals(MessageHandler.getMessage("message.settings.language", language))) {

				}

				send(message);
				break;

			case MATERIALS_MENU:
				send(menuHandler.materialsMenu(update, language));
				break;
			}
		}

		else if (update.hasCallbackQuery()) {

			CallbackQuery callBackQuery = update.getCallbackQuery();

			String callBackData = update.getCallbackQuery().getData();

			long chatId = callBackQuery.getMessage().getChatId();
			long messageId = callBackQuery.getMessage().getMessageId();

			String language = service.getUserLanguage(chatId);

			UserState userState = service.getUserState(chatId);

			EditMessageText message = new EditMessageText();
			message.setChatId(String.valueOf(chatId));

			message.setMessageId((int) messageId);

			if (userState.equals(UserState.SETTINGS_MENU) || userState.equals(UserState.WORK_MENU)) {

				if (isCallBackDataIsFaculty(callBackData)) {

					service.setUserFaculty(chatId, callBackData.substring(8));

					if (service.isUserSpecializationSet(chatId) && userState.equals(UserState.WORK_MENU)) {
						message.setText(MessageHandler.getMessage("message.work.subject.specialization", language));
						message.setReplyMarkup(InlineKeyboardFactory.createSpecializationPage(language));
					}

					else {
						message.setText(MessageHandler.getMessage("message.sucess", language));
					}

				}

				else if (isCallBackDataIsSpecialization(callBackData)) {

					service.setUserSpecialization(chatId,
							callBackData.substring(16 + service.getUserFaculty(chatId).length()));

					if (service.isUserSemesterSet(chatId) && userState.equals(UserState.WORK_MENU)) {
						message.setText(MessageHandler.getMessage("message.work.subject.semester", language));
						message.setReplyMarkup(InlineKeyboardFactory.createSemesterPage(language));
					}

					else {
						message.setText(MessageHandler.getMessage("message.sucess", language));
					}

				}

				else if (isCallBackDataIsCourse(callBackData)) {

					service.setUserCourse(chatId, callBackData.substring(9));

					message.setText(MessageHandler.getMessage("message.sucess", language));

				}

				else if (userState.equals(UserState.SETTINGS_MENU)) {

					if (callBackData.equals("faculty")) {

						message.setText(MessageHandler.getMessage("message.work.subject.faculty", language));
						message.setReplyMarkup(InlineKeyboardFactory.createFacultyPage(language));

					}

					else if (callBackData.equals("semester")) {

						message.setText(MessageHandler.getMessage("message.work.subject.semester", language));
						message.setReplyMarkup(InlineKeyboardFactory.createSemesterPage(language));

					}

					else if (callBackData.equals("specialization")) {

						message.setText(MessageHandler.getMessage("message.work.subject.specialization", language));
						message.setReplyMarkup(InlineKeyboardFactory.createSpecializationPage(language));

					}

					else if (isChangeLanguage(callBackData)) {

						String newLanguage = MessageHandler.getMessage("code." + callBackData, language);

						service.setUserLanguage(chatId, newLanguage);

						message.setText(MessageHandler.getMessage("message.sucess", language));
					}

				}

			}

			else {

				message.setText(MessageHandler.getMessage("message.failed", language));
			}

			sendEmessage(message);
		}

	}

	private boolean isChangeLanguage(String callBackData) {

		return callBackData.equals("ukrainian") || callBackData.equals("russian") || callBackData.equals("english");
	}

	private boolean isCallBackDataIsFaculty(String callBackData) {

		return callBackData.contains("faculty.");
	}

	private boolean isCallBackDataIsSpecialization(String callBackData) {

		return callBackData.contains("specialization.");
	}

	private boolean isCallBackDataIsCourse(String callBackData) {

		return callBackData.contains("semester.");
	}

	private void firstLaunch(Update update) {
		service.registerUser(update);
	}

	private void sendEmessage(EditMessageText message) {
		try {
			execute(message);
		} catch (Exception e) {

		}
	}

	private void send(SendMessage message) {
		try {
			execute(message);
		} catch (TelegramApiException e) {
			Logger.logError(e);
		}
	}

	@Override
	public String getBotUsername() {
		return config.getBotName();
	}

	@Override
	public String getBotToken() {
		return config.getBotToken();
	}

}
