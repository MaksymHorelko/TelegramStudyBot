package ua.gexlq.TelegramStudyBot.process.text.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.dao.AppDataDAO;
import ua.gexlq.TelegramStudyBot.dao.AppUserDAO;
import ua.gexlq.TelegramStudyBot.entity.AppUser;
import ua.gexlq.TelegramStudyBot.entity.enums.Languages;
import ua.gexlq.TelegramStudyBot.entity.enums.UserState;
import ua.gexlq.TelegramStudyBot.keyboard.menu.menus.HelpMenu;
import ua.gexlq.TelegramStudyBot.keyboard.menu.menus.MainMenu;
import ua.gexlq.TelegramStudyBot.process.text.ProcessMessageByUserState;
import ua.gexlq.TelegramStudyBot.process.text.enums.HelpCommand;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;
import ua.gexlq.TelegramStudyBot.utils.UserPermissionsService;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ProcessHelpState implements ProcessMessageByUserState {

	private final AppUserDAO appUserDAO;
	private final AppDataDAO appDataDAO;
	private final UserInfo userInfo;
	private final MessageUtils messageUtils;
	private final UserPermissionsService permissionsService;

	private Map<String, HelpCommand> commandToEnumMapping = new HashMap<>();

	private final HelpMenu helpMenu;
	private final MainMenu mainMenu;

	@Value("${folder.contactGroupId}")
	private String contactGroupId;

	private final String MENU_HELP_RULES = "menu.help.rules";
	private final String MENU_HELP_DISCALIMER = "menu.help.disclaimer";
	private final String MENU_HELP_GITHUB = "menu.help.github";
	private final String MENU_HELP_DONATE = "menu.help.donate";
	private final String MENU_HELP_CONTACT = "menu.help.contact";
	private final String MENU_HELP_CHAT = "menu.help.chat";
	private final String MENU_BACK = "menu.back";

	private final String MESSAGE_HELP_RULES = "message.help.rules";
	private final String MESSAGE_HELP_DISCLAIMER = "message.help.disclaimer";
	private final String MESSAGE_HELP_GITHUB = "message.help.github";
	private final String MESSAGE_HELP_DONATE = "message.help.donate";
	private final String MESSAGE_HELP_CONTACT = "message.help.contact";
	private final String MESSAGE_HELP_CHAT = "message.help.chat";

	private final String MESSAGE_HELP_CONTACTS_LIMIT_REACHED = "message.help.contactsLimitReached";
	private final String MESSAGE_STEP_BACK_TO_MAIN_MENU = "message.stepBackToMainMenu";
	private final String MESSAGE_UNKNOWN_COMMAND = "message.unknownCommand";
	private final String MESSAGE_CONTACT_FORM = "message.help.contactForm";

	@Override
	public SendMessage handle(Update update) {
		long chatId = update.getMessage().getChatId();
		String messageText = update.getMessage().getText();
		String language = userInfo.getUserLanguage(chatId);

		initMap(language);

		SendMessage response;

		HelpCommand helpCommand = getHelpCommand(messageText);

		if (helpCommand == null && userInfo.isUserAbleToSendMesToSupport(update.getMessage().getChatId()))
			helpCommand = HelpCommand.CONTACT_MESSAGE;

		else if (userInfo.isUserAbleToSendMesToSupport(update.getMessage().getChatId()))
			permissionsService.setContactPermission(update, false);

		if (helpCommand != null) {
			response = handleCommand(update, helpCommand, language);
		} else {
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UNKNOWN_COMMAND);
			response.setReplyMarkup(helpMenu.createHelpMenuKeyboard(language));
		}

		return response;
	}

	private SendMessage handleCommand(Update update, HelpCommand helpCommand, String language) {
		SendMessage response;

		switch (helpCommand) {

		case RULES:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_HELP_RULES);
			break;

		case DISCLAIMER:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_HELP_DISCLAIMER);
			break;

		case GITHUB:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_HELP_GITHUB);
			response.setParseMode("HTML");
			break;

		case DONATE:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_HELP_DONATE);
			response.setParseMode("HTML");
			break;

		case CONTACT:
			var user = appUserDAO.findUserByTelegramUserId(update.getMessage().getChatId());
			var userPermissions = user.getPermissions();
			if (userPermissions.getContactsToday() >= appDataDAO.getAppData().getMaxContactsPerDay()) {
				response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_HELP_CONTACTS_LIMIT_REACHED);
				break;
			}

			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_HELP_CONTACT);
			userPermissions.setAbleToSendMesToSupport(true);
			user.setPermissions(userPermissions);
			appUserDAO.save(user);

			break;

		case CHAT:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_HELP_CHAT);
			response.setParseMode("HTML");
			break;

		case CONTACT_MESSAGE:
			String contactForm = messageUtils.getAnswerTextByCode(MESSAGE_CONTACT_FORM, Languages.UKRANIAN);

			long chatId = update.getMessage().getChatId();
			long telegramId = update.getMessage().getFrom().getId();
			String text = update.getMessage().getText();

			String username = update.getMessage().getFrom().getUserName();
			String firstName = update.getMessage().getFrom().getFirstName();
			String lastName = update.getMessage().getFrom().getLastName() == null ? "null"
					: update.getMessage().getFrom().getLastName();
			String data = new Timestamp(System.currentTimeMillis()).toString();

			String faculty = userInfo.getUserFaculty(chatId) == null ? "null"
					: messageUtils.getAnswerTextByCode("faculty." + userInfo.getUserFaculty(chatId),
							Languages.UKRANIAN);

			String specialization = userInfo.getUserSpecialization(chatId) == null ? "null"
					: userInfo.getUserSpecialization(chatId);

			String semester = userInfo.getUserSemester(chatId) == null ? "null" : userInfo.getUserSemester(chatId);

			String filledForm = contactForm.replace("{firstname}", firstName).replace("{lastname}", lastName)
					.replace("{nickname}", username).replace("{faculty}", faculty)
					.replace("{specialization}", specialization).replace("{semester}", semester).replace("{data}", data)
					.replace("{text}", text).replace("{chatId}", String.valueOf(chatId))
					.replace("{telegramId}", String.valueOf(telegramId))
					.replace("{warnings}", String.valueOf(userInfo.getUserWarnings(chatId)));

			response = messageUtils.createSendMessageWithText(Long.valueOf(contactGroupId), filledForm);

			permissionsService.addNewContact(update);
			break;

		case BACK:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_STEP_BACK_TO_MAIN_MENU);
			response.setReplyMarkup(mainMenu.createMainMenuKeyboard(language));
			setNewState(update, UserState.MAIN_STATE);
			break;

		default:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UNKNOWN_COMMAND);
			response.setReplyMarkup(helpMenu.createHelpMenuKeyboard(language));
		}

		return response;
	}

	private void initMap(String language) {
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_HELP_RULES, language), HelpCommand.RULES);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_HELP_DISCALIMER, language),
				HelpCommand.DISCLAIMER);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_HELP_GITHUB, language), HelpCommand.GITHUB);
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
