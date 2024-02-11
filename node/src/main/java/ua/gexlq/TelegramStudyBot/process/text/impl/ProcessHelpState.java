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
import ua.gexlq.TelegramStudyBot.process.text.enums.HelpCommand;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ProcessHelpState implements ProcessMessageByUserState {

	private final AppUserDAO appUserDAO;
	private final UserInfo userInfo;
	private final KeyboardFactory keyboardFactory;
	private final MessageUtils messageUtils;

	private Map<String, HelpCommand> commandToEnumMapping = new HashMap<>();

	private static final String MENU_HELP_RULES = "menu.help.rules";
	private static final String MENU_HELP_DONATE = "menu.help.donate";
	private static final String MENU_HELP_CONTACT = "menu.help.contact";
	private static final String MENU_HELP_CHAT = "menu.help.chat";
	private static final String MENU_BACK = "menu.back";

	private static final String MESSAGE_HELP_RULES = "message.help.rules";
	private static final String MESSAGE_HELP_DONATE = "message.help.donate";
	private static final String MESSAGE_HELP_CONTACT = "message.help.contact";
	private static final String MESSAGE_HELP_CHAT = "message.help.chat";
	private static final String MESSAGE_STEP_BACK_TO_MAIN_MENU = "message.stepBackToMainMenu";
	private static final String MESSAGE_UNKNOWN_COMMAND = "message.unknownCommand";

	@Override
	public SendMessage handle(Update update) {
		long chatId = update.getMessage().getChatId();
		String messageText = update.getMessage().getText();
		String language = userInfo.getUserLanguage(chatId);

		initMap(language);

		SendMessage response;

		HelpCommand helpCommand = getHelpCommand(messageText);
		if (helpCommand != null) {
			response = handleCommand(update, helpCommand, language);
		} else {
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UNKNOWN_COMMAND);
			response.setReplyMarkup(keyboardFactory.createHelpMenuKeyboard(language));
		}

		return response;
	}

	private SendMessage handleCommand(Update update, HelpCommand helpCommand, String language) {
		SendMessage response;

		switch (helpCommand) {
		case RULES:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_HELP_RULES);
			break;

		case DONATE:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_HELP_DONATE);
			break;

		case CONTACT:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_HELP_CONTACT);
			break;

		case CHAT:
			String chatURLMessage = String.format(messageUtils.getAnswerTextByCode(MESSAGE_HELP_CHAT, language),
					messageUtils.getAnswerTextByCode("chatURL", language));
			response = messageUtils.createSendMessageWithText(update, chatURLMessage);
			break;

		case BACK:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_STEP_BACK_TO_MAIN_MENU);
			response.setReplyMarkup(keyboardFactory.createMainMenuKeyboard(language));
			setNewState(update, UserState.MAIN_STATE);
			break;

		default:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UNKNOWN_COMMAND);
			response.setReplyMarkup(keyboardFactory.createHelpMenuKeyboard(language));
		}

		return response;
	}

	private void initMap(String language) {
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_HELP_RULES, language), HelpCommand.RULES);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_HELP_DONATE, language), HelpCommand.DONATE);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_HELP_CONTACT, language), HelpCommand.CONTACT);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_HELP_CHAT, language), HelpCommand.CHAT);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_BACK, language), HelpCommand.BACK);
	}

	private HelpCommand getHelpCommand(String messageText) {
		return commandToEnumMapping.get(messageText);
	}

	private void setNewState(Update update, UserState state) {
		AppUser user = appUserDAO.findUserByTelegramUserId(update.getMessage().getChatId());
		user.setUserState(state);
		appUserDAO.save(user);
	}

	@Override
	public UserState getSupportedState() {
		return UserState.HELP_STATE;
	}
}
