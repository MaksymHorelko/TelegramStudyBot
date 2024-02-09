package ua.gexlq.TelegramStudyBot.process.text.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.entity.AppUser;
import ua.gexlq.TelegramStudyBot.entity.enums.UserState;
import ua.gexlq.TelegramStudyBot.keyboard.KeyboardFactory;
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
	private final KeyboardFactory keyboardFactory;
	private final MessageUtils messageUtils;

	private Map<String, MainCommand> commandToEnumMapping = new HashMap<>();

	private static final String START_COMMAND = "/start";
	private static final String MENU_WORKS = "menu.works.name";
	private static final String MENU_MATERIALS = "menu.materials.name";
	private static final String MENU_HELP = "menu.help.name";
	private static final String MENU_SETTINGS = "menu.settings.name";

	private static final String MESSAGE_WELCOME = "message.welcome.name";
	private static final String MESSAGE_WORKS = "message.works.name";
	private static final String MESSAGE_MATERIALS = "message.materials.name";
	private static final String MESSAGE_HELP = "message.help.name";
	private static final String MESSAGE_SETTINGS = "message.settings.name";
	private static final String MESSAGE_UNKNOWN_COMMAND = "message.unknownCommand";

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
			response.setReplyMarkup(keyboardFactory.createMainMenuKeyboard(language));
		}

		return response;
	}

	private SendMessage handleCommand(Update update, MainCommand mainCommand, String language) {
		SendMessage response;
		long chatId = update.getMessage().getChatId();

		switch (mainCommand) {

		case START:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_WELCOME);
			response.setReplyMarkup(keyboardFactory.createMainMenuKeyboard(language));
			break;

		case WORKS:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_WORKS);
			response.setReplyMarkup(keyboardFactory.createWorksMenuKeyboard(language));
			setNewState(update, UserState.WORKS_STATE);
			break;

		case MATERIALS:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_MATERIALS);
			response.setReplyMarkup(keyboardFactory.createMaterialsMenuKeyboard(language));
			setNewState(update, UserState.MATERIALS_STATE);
			break;

		case HELP:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_HELP);
			response.setReplyMarkup(keyboardFactory.createHelpMenuKeyboard(language));
			setNewState(update, UserState.HELP_STATE);
			break;

		case SETTINGS:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_SETTINGS);
			response.setReplyMarkup(
					keyboardFactory.createSettingsMenuKeyboard(userInfo.isUserDataSet(chatId), language));
			setNewState(update, UserState.SETTINGS_STATE);
			break;

		default:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UNKNOWN_COMMAND);
			response.setReplyMarkup(keyboardFactory.createMainMenuKeyboard(language));
		}

		return response;
	}

	private void initMap(String language) {
		commandToEnumMapping.put(START_COMMAND, MainCommand.START);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_WORKS, language), MainCommand.WORKS);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_MATERIALS, language), MainCommand.MATERIALS);
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