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
import ua.gexlq.TelegramStudyBot.process.text.enums.MaterialsCommand;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

@RequiredArgsConstructor
@Component
public class ProcessMaterialsState implements ProcessMessageByUserState {

	private final AppUserDAO appUserDAO;
	private final UserInfo userInfo;
	private final KeyboardFactory keyboardFactory;
	private final InlineKeyboardFactory inlineKeyboardFactory;
	private final MessageUtils messageUtils;

	private Map<String, MaterialsCommand> commandToEnumMapping = new HashMap<>();

	private static final String MENU_MATERIALS_LECTURE = "menu.materials.lectures";
	private static final String MENU_MATERIALS_SEMESTER = "menu.materials.semester";
	private static final String MENU_MATERIALS_LITERATURE = "menu.materials.literature";
	private static final String MENU_BACK = "menu.back";

	private static final String MESSAGE_MATERIALS_LECTURE = "message.materials.lectures";
	private static final String MESSAGE_MATERIALS_SEMESTER = "message.materials.semester";
	private static final String MESSAGE_MATERIALS_LITERATURE = "message.materials.literature";
	private static final String MESSAGE_STEP_BACK_TO_MAIN_MENU = "message.stepBackToMainMenu";
	private static final String MESSAGE_UNKNOWN_COMMAND = "message.unknownCommand";

	@Override
	public SendMessage handle(Update update) {
		long chatId = update.getMessage().getChatId();
		String messageText = update.getMessage().getText();
		String language = userInfo.getUserLanguage(chatId);

		initMap(language);

		SendMessage response;

		MaterialsCommand materialsCommand = getMaterialsCommand(messageText);

		if (materialsCommand != null) {
			response = handleCommand(update, materialsCommand, language);
		} else {
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UNKNOWN_COMMAND);
			response.setReplyMarkup(keyboardFactory.createMaterialsMenuKeyboard(language));
		}

		return response;
	}

	private SendMessage handleCommand(Update update, MaterialsCommand materialsCommand, String language) {
		SendMessage response;

		switch (materialsCommand) {

		case LECTURES:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_MATERIALS_LECTURE);
			break;

		case LITERATURE:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_MATERIALS_LITERATURE);
			break;

		case SEMESTER:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_MATERIALS_SEMESTER);
			break;

		case BACK:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_STEP_BACK_TO_MAIN_MENU);
			response.setReplyMarkup(keyboardFactory.createMainMenuKeyboard(language));
			setNewState(update, UserState.MAIN_STATE);
			break;

		default:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UNKNOWN_COMMAND);
			response.setReplyMarkup(keyboardFactory.createMaterialsMenuKeyboard(language));
		}

		return response;
	}

	private void initMap(String language) {
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_MATERIALS_LECTURE, language),
				MaterialsCommand.LECTURES);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_MATERIALS_LITERATURE, language),
				MaterialsCommand.LITERATURE);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_MATERIALS_SEMESTER, language),
				MaterialsCommand.SEMESTER);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_BACK, language), MaterialsCommand.BACK);
	}

	private MaterialsCommand getMaterialsCommand(String messageText) {
		return commandToEnumMapping.get(messageText);
	}

	private void setNewState(Update update, UserState state) {
		AppUser user = appUserDAO.findUserByTelegramUserId(update.getMessage().getChatId());
		user.setUserState(state);
		appUserDAO.save(user);
	}

	@Override
	public UserState getSupportedState() {
		return UserState.MATERIALS_STATE;
	}

}