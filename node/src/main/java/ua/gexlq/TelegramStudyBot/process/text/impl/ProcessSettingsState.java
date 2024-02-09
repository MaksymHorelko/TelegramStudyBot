package ua.gexlq.TelegramStudyBot.process.text.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.entity.AppUser;
import ua.gexlq.TelegramStudyBot.entity.enums.UserState;
import ua.gexlq.TelegramStudyBot.keyboard.InlineKeyboardFactory;
import ua.gexlq.TelegramStudyBot.keyboard.KeyboardFactory;
import ua.gexlq.TelegramStudyBot.process.text.ProcessMessageByUserState;
import ua.gexlq.TelegramStudyBot.process.text.enums.SettingsCommand;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessSettingsState implements ProcessMessageByUserState {

	private final AppUserDAO appUserDAO;
	private final UserInfo userInfo;
	private final KeyboardFactory keyboardFactory;
	private final InlineKeyboardFactory inlineKeyboardFactory;
	private final MessageUtils messageUtils;

	private Map<String, SettingsCommand> commandToEnumMapping = new HashMap<>();

	private static final String MENU_SETTINGS_LANGUAGE = "menu.settings.language";
	private static final String MENU_SETTINGS_NOTIFICATION = "menu.settings.notifications";
	private static final String MENU_SETTINGS_DATA = "menu.settings.data";
	private static final String MENU_BACK = "menu.back";

	private static final String MESSAGE_SETTINGS_LANGUAGE = "message.settings.language";
	private static final String MESSAGE_SETTINGS_NOTIFICATION = "message.settings.notifications";
	private static final String MESSAGE_SETTINGS_DATA = "message.settings.data";
	private static final String MESSAGE_STEP_BACK_TO_MAIN_MENU = "message.stepBackToMainMenu";
	private static final String MESSAGE_UNKNOWN_COMMAND = "message.unknownCommand";

	@Override
	public SendMessage handle(Update update) {
		long chatId = update.getMessage().getChatId();
		String messageText = update.getMessage().getText();
		String language = userInfo.getUserLanguage(chatId);

		initMap(language);

		SendMessage response;

		SettingsCommand settingsCommand = getSettingsCommand(messageText);

		if (settingsCommand != null) {
			response = handleCommand(update, settingsCommand, language);
		} else {
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UNKNOWN_COMMAND);
			response.setReplyMarkup(
					keyboardFactory.createSettingsMenuKeyboard(userInfo.isUserDataSet(chatId), language));
		}

		return response;
	}

	private SendMessage handleCommand(Update update, SettingsCommand settingsCommand, String language) {
		SendMessage response;
		long chatId = update.getMessage().getChatId();

		switch (settingsCommand) {

		case LANGUAGE:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_SETTINGS_LANGUAGE);
			response.setReplyMarkup(inlineKeyboardFactory.createChangeLanguagePage(language));
			break;

		case NOTIFICATION:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_SETTINGS_NOTIFICATION);
			break;

		case DATA:
			AppUser user = appUserDAO.findUserByTelegramUserId(update.getMessage().getChatId());

			String dataAnswer = messageUtils.getAnswerTextByCode(MESSAGE_SETTINGS_DATA, language)
					.replace("{0}", messageUtils.getAnswerTextByCode("faculty" + "." + user.getFaculty(), language))
					.replace("{1}", user.getSpecialization()).replace("{2}", user.getSemester());

			user.setCurrentActiveMessageId(String.valueOf(update.getMessage().getMessageId() + 1));
			appUserDAO.save(user);
			
			response = messageUtils.createSendMessageWithText(update, dataAnswer);
			response.setReplyMarkup(inlineKeyboardFactory.createChangeDataPage(language));
			break;

		case BACK:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_STEP_BACK_TO_MAIN_MENU);
			response.setReplyMarkup(keyboardFactory.createMainMenuKeyboard(language));
			setNewState(update, UserState.MAIN_STATE);
			break;

		default:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UNKNOWN_COMMAND);
			response.setReplyMarkup(
					keyboardFactory.createSettingsMenuKeyboard(userInfo.isUserDataSet(chatId), language));
		}

		return response;
	}

	private void initMap(String language) {
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_SETTINGS_LANGUAGE, language),
				SettingsCommand.LANGUAGE);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_SETTINGS_NOTIFICATION, language),
				SettingsCommand.NOTIFICATION);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_SETTINGS_DATA, language), SettingsCommand.DATA);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_BACK, language), SettingsCommand.BACK);
	}

	private SettingsCommand getSettingsCommand(String messageText) {
		return commandToEnumMapping.get(messageText);
	}

	private void setNewState(Update update, UserState state) {
		AppUser user = appUserDAO.findUserByTelegramUserId(update.getMessage().getChatId());
		user.setUserState(state);
		appUserDAO.save(user);
	}

	@Override
	public UserState getSupportedState() {
		return UserState.SETTINGS_STATE;
	}

}