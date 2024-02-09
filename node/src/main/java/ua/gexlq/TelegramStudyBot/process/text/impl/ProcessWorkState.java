package ua.gexlq.TelegramStudyBot.process.text.impl;

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
import ua.gexlq.TelegramStudyBot.process.text.enums.WorksCommand;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ProcessWorkState implements ProcessMessageByUserState {

	private final AppUserDAO appUserDAO;
	private final UserInfo userInfo;
	private final KeyboardFactory keyboardFactory;
	private final InlineKeyboardFactory inlineKeyboardFactory;
	private final MessageUtils messageUtils;

	private Map<String, WorksCommand> commandToEnumMapping = new HashMap<>();

	private static final String MENU_WORKS_WORKS = "menu.works.works";
	private static final String MENU_WORKS_VIEW = "menu.works.view";

	private static final String MESSAGE_PICK_SUBJECT = "message.pick.subject";
	private static final String MESSAGE_PICK_FACULTY = "message.pick.faculty";
	private static final String MESSAGE_PICK_SPECIALIZATION = "message.pick.specialization";
	private static final String MESSAGE_PICK_SEMESTER = "message.pick.semester";
	private static final String MENU_BACK = "menu.back";

	private static final String MESSAGE_WORKS_VIEW = "message.works.view";
	private static final String MESSAGE_STEP_BACK_TO_MAIN_MENU = "message.stepBackToMainMenu";
	private static final String MESSAGE_UNKNOWN_COMMAND = "message.unknownCommand";

	@Override
	public SendMessage handle(Update update) {
		long chatId = update.getMessage().getChatId();

		String messageText = update.getMessage().getText();
		String language = userInfo.getUserLanguage(chatId);

		initMap(language);

		SendMessage response;

		WorksCommand worksCommand = getWorksCommand(messageText);
		if (worksCommand != null) {
			response = handleCommand(update, worksCommand, language);
		} else {
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UNKNOWN_COMMAND);
			response.setReplyMarkup(keyboardFactory.createWorksMenuKeyboard(language));
		}

		return response;
	}

	private SendMessage handleCommand(Update update, WorksCommand worksCommand, String language) {
		SendMessage response;
		long chatId = update.getMessage().getChatId();

		switch (worksCommand) {

		case WORKS:

			if (userInfo.isUserFacultySet(chatId)) {
				response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_PICK_FACULTY);
				response.setReplyMarkup(inlineKeyboardFactory.createFacultyPage(language));
			}

			else if (userInfo.isUserSpecializationSet(chatId)) {
				String faculty = userInfo.getUserFaculty(chatId);
				response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_PICK_SPECIALIZATION);
				response.setReplyMarkup(inlineKeyboardFactory.createSpecializationPage(faculty, language));
			}

			else if (userInfo.isUserSemesterSet(chatId)) {
				response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_PICK_SEMESTER);
				response.setReplyMarkup(inlineKeyboardFactory.createSemesterPage(language));
			}

			else {
				response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_PICK_SUBJECT);

				response.setReplyMarkup(inlineKeyboardFactory.createSubjectPage(userInfo.getUserFaculty(chatId),
						userInfo.getUserSpecialization(chatId), userInfo.getUserSemester(chatId), language));

				var user = appUserDAO.findUserByTelegramUserId(update.getMessage().getFrom().getId());
				user.setCurrentActiveMessageId(String.valueOf(update.getMessage().getMessageId() + 1));
				appUserDAO.save(user);

			}
			break;

		case VIEW:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_WORKS_VIEW);
			break;

		case BACK:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_STEP_BACK_TO_MAIN_MENU);
			response.setReplyMarkup(keyboardFactory.createMainMenuKeyboard(language));
			setNewState(update, UserState.MAIN_STATE);
			break;

		default:
			response = messageUtils.createSendMessageWithAnswerCode(update, MESSAGE_UNKNOWN_COMMAND);
			response.setReplyMarkup(keyboardFactory.createWorksMenuKeyboard(language));
		}

		return response;
	}

	private void initMap(String language) {
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_WORKS_WORKS, language), WorksCommand.WORKS);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_WORKS_VIEW, language), WorksCommand.VIEW);
		commandToEnumMapping.put(messageUtils.getAnswerTextByCode(MENU_BACK, language), WorksCommand.BACK);
	}

	private WorksCommand getWorksCommand(String messageText) {
		return commandToEnumMapping.get(messageText);
	}

	private void setNewState(Update update, UserState state) {
		AppUser user = appUserDAO.findUserByTelegramUserId(update.getMessage().getChatId());
		user.setUserState(state);
		appUserDAO.save(user);
	}

	@Override
	public UserState getSupportedState() {
		return UserState.WORKS_STATE;
	}
}
