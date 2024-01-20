package ua.gexlq.TelegramStudyBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ua.gexlq.TelegramStudyBot.config.Config;
import ua.gexlq.TelegramStudyBot.handler.MessageHandler;
import ua.gexlq.TelegramStudyBot.handler.RequestHandler;
import ua.gexlq.TelegramStudyBot.handler.Logger;
import ua.gexlq.TelegramStudyBot.handler.MenuHandler;
import ua.gexlq.TelegramStudyBot.model.UserService;
import ua.gexlq.TelegramStudyBot.model.UserService.UserState;

@Component
public class TelegramBot extends TelegramLongPollingBot {

	@Autowired
	private UserService service;

	@Autowired
	private RequestHandler requestHandler;

	@Autowired
	private MenuHandler menuHandler;

	private final Config config;

	public TelegramBot(Config config) {
		this.config = config;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			handleMessage(update.getMessage());
		} else if (update.hasCallbackQuery()) {
			handleCallbackQuery(update.getCallbackQuery());
		}
	}

	private void handleMessage(Message message) {
		long chatId = message.getChatId();

		if (message.getText().equals("/start") && !service.isUserRegister(chatId)) {
			firstLaunch(message);
		}
		
		UserState userState = service.getUserState(chatId);
		String language = service.getUserLanguage(chatId);

		switch (userState) {

		case MAIN_MENU:
			send(menuHandler.mainMenu(message, language));
			break;

		case WORK_MENU:
			send(menuHandler.workMenu(message, language));
			break;

		case HELP_MENU:
			send(menuHandler.helpMenu(message, language));
			break;

		case SETTINGS_MENU:
			SendMessage sendMessage = menuHandler.settingsMenu(message, language);

			if (message.getText().equals(MessageHandler.getMessage("message.settings.language", language))) {

			}

			send(sendMessage);
			break;

		case MATERIALS_MENU:
			send(menuHandler.materialsMenu(message, language));
			break;
		}
	}

	private void handleCallbackQuery(CallbackQuery callBackQuery) {
		String callBackData = callBackQuery.getData();
		long chatId = callBackQuery.getMessage().getChatId();
		long messageId = callBackQuery.getMessage().getMessageId();
		String language = service.getUserLanguage(chatId);
		UserState userState = service.getUserState(chatId);

		EditMessageText editMessage = new EditMessageText();
		editMessage.setChatId(String.valueOf(chatId));
		editMessage.setMessageId((int) messageId);

		System.out.println("callBackData = " + callBackData + "\n\n\n\n");

		if (userState.equals(UserState.SETTINGS_MENU) || userState.equals(UserState.WORK_MENU)) {
			if (ConditionChecker.isCallBackDataIsSubject(callBackData)) {
				String subject = callBackData.substring(callBackData.indexOf(">") + 1, callBackData.length());

				editMessage.setText(MessageHandler.getMessage("message.pickWorkType", language));
				editMessage.setReplyMarkup(InlineKeyboardFactory.createWorkTypePage(subject, language));
			}

			else if (ConditionChecker.isCallBackDataIsWorkType(callBackData)) {
				String subject = callBackData.substring(callBackData.indexOf(">") + 1, callBackData.lastIndexOf("."));
				String workType = callBackData.substring(callBackData.lastIndexOf(".") + 1, callBackData.length());

				editMessage.setText(MessageHandler.getMessage("message.pickWork", language));
				editMessage.setReplyMarkup(InlineKeyboardFactory.createPickWorkPage(subject, workType, language));
			}

			else if (ConditionChecker.isCallBackDataIsWork(callBackData)) {
				String selectedWork = callBackData.substring(callBackData.indexOf(">") + 1, callBackData.length());

				editMessage.setText(MessageHandler.getMessage("message.pickOption", language));
				editMessage.setReplyMarkup(InlineKeyboardFactory.createWorkOptionPage(selectedWork, language));
			}

			//
			//

			else if (ConditionChecker.isCallBackDataIsUpload(callBackData)) {

			}

			else if (ConditionChecker.isCallBackDataIsDownload(callBackData)) {

			}

			else if (ConditionChecker.isCallBackDataIsBack(callBackData)) {

			}

			//
			//

			else if (ConditionChecker.isCallBackDataIsFaculty(callBackData)) {

				service.setUserFaculty(chatId,
						callBackData.substring(callBackData.indexOf(">") + 1, callBackData.length()));

				if (!service.isUserSpecializationSet(chatId) && userState.equals(UserState.WORK_MENU)) {
					setSpecialization(editMessage, chatId, language);
				}

				else {
					editMessage.setText(MessageHandler.getMessage("message.sucess", language));
				}

			}

			else if (ConditionChecker.isCallBackDataIsSpecialization(callBackData)) {
				String specialization = callBackData.substring(callBackData.indexOf(">") + 1, callBackData.length());
				service.setUserSpecialization(chatId, specialization);

				if (!service.isUserSemesterSet(chatId) && userState.equals(UserState.WORK_MENU)) {
					editMessage.setText(MessageHandler.getMessage("message.pickSemester", language));
					editMessage.setReplyMarkup(InlineKeyboardFactory.createSemesterPage(language));
				}

				else {
					editMessage.setText(MessageHandler.getMessage("message.sucess", language));
				}

			}

			else if (ConditionChecker.isCallBackDataIsSemester(callBackData)) {
				String semester = callBackData.substring(callBackData.indexOf(">") + 1, callBackData.length());
				service.setUserSemester(chatId, semester);

				if (userState.equals(UserState.WORK_MENU)) {
					editMessage.setText(MessageHandler.getMessage("message.isCorrect", language) + "\n\n"
							+ MessageHandler.getMessage("message.works.subject", language));
					editMessage.setReplyMarkup(InlineKeyboardFactory.createSubjectPage(service.getUserFaculty(chatId),
							service.getUserSpecialization(chatId), service.getUserSemester(chatId), language));
				} else
					editMessage.setText(MessageHandler.getMessage("message.sucess", language));
			}

			else if (userState.equals(UserState.SETTINGS_MENU)) {

				if (callBackData.equals("changeFaculty")) {

					editMessage.setText(MessageHandler.getMessage("message.pickFaculty", language));
					editMessage.setReplyMarkup(InlineKeyboardFactory.createFacultyPage(language));

				}

				else if (callBackData.equals("changeSpecialization")) {
					setSpecialization(editMessage, chatId, language);
				}

				else if (callBackData.equals("changeSemester")) {

					editMessage.setText(MessageHandler.getMessage("message.pickSemester", language));
					editMessage.setReplyMarkup(InlineKeyboardFactory.createSemesterPage(language));

				}

				else if (ConditionChecker.isChangeLanguage(callBackData)) {

					String newLanguage = MessageHandler.getMessage(callBackData + "." + "code", language);

					service.setUserLanguage(chatId, newLanguage);

					editMessage.setText(MessageHandler.getMessage("message.sucess", language));
				}

			}

		}

		else {

			editMessage.setText(MessageHandler.getMessage("message.failed", language));
		}

		if (!editMessage.getText().isEmpty())
			sendEditMessage(editMessage);
	}

	private void setSpecialization(EditMessageText editMessage, long chatId, String language) {
		String faculty = service.getUserFaculty(chatId);
		editMessage.setText(MessageHandler.getMessage("message.pickSpecialization", language));
		editMessage.setReplyMarkup(InlineKeyboardFactory.createSpecializationPage(faculty, language));
	}

	private void firstLaunch(Message message) {
		service.registerUser(message);
	}

	private void sendEditMessage(EditMessageText message) {
		try {
			execute(message);
		} catch (Exception e) {
			Logger.logError(e);
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
