package ua.gexlq.TelegramStudyBot.process.text;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.entity.AppUser;
import ua.gexlq.TelegramStudyBot.entity.enums.UserState;
import ua.gexlq.TelegramStudyBot.exceptions.TextServiceException;
import ua.gexlq.TelegramStudyBot.keyboard.menu.menus.HelpMenu;
import ua.gexlq.TelegramStudyBot.process.text.impl.ProcessCheckGroup;
import ua.gexlq.TelegramStudyBot.service.ProducerService;
import ua.gexlq.TelegramStudyBot.utils.MessageUtils;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;
import ua.gexlq.TelegramStudyBot.utils.UserPermissionsService;

@RequiredArgsConstructor
@Service
public class TextMessageService {

	private final ProducerService producerService;
	private final UserPermissionsService permissionsService;
	private final MessageUtils messageUtils;
	private final HelpMenu helpMenu;
	private final UserInfo userInfo;

	private final ProcessCheckGroup processCheckGroup;
	private final List<ProcessMessageByUserState> processesState;

	@Value("${folder.checkGroupId}")
	private String checkGroupId;

	@Value("${folder.contactGroupId}")
	private String contactGroupId;

	private final String MESSAGE_HELP_CONTACT_SUCCESS = "message.help.contactSuccess";

	public void handleUserChat(AppUser user, Update update) {
		if (userInfo.isUserAbleToSendFile(update.getMessage().getChatId()))
			permissionsService.setFileUploadPermission(update, false);

		var response = handleUserTextMessage(user.getUserState(), update);
		producerService.produceAnswer(response);

		if (response.getChatId().equals(contactGroupId)) {
			sendSupportMessageSuccessfully(update);
		}
	}

	public void handleCheckGroupChat(Update update) {
		var response = processCheckGroup.handle(update);
		producerService.produceAnswer(response);
	}

	public boolean isCheckGroupChat(Update update) {
		return checkGroupId.equals(update.getMessage().getChatId().toString());
	}

	private SendMessage handleUserTextMessage(UserState currentUserState, Update update) {
		for (ProcessMessageByUserState stateProcessor : processesState) {
			if (stateProcessor.getSupportedState().equals(currentUserState)) {
				return stateProcessor.handle(update);
			}
		}
		throw new TextServiceException("Unsupported state: " + currentUserState);
	}

	private void sendSupportMessageSuccessfully(Update update) {
		var suggestionSentSuccessfully = messageUtils.createSendMessageWithAnswerCode(update,
				MESSAGE_HELP_CONTACT_SUCCESS);
		suggestionSentSuccessfully.setReplyMarkup(
				helpMenu.createHelpMenuKeyboard(userInfo.getUserLanguage(update.getMessage().getChatId())));
		producerService.produceAnswer(suggestionSentSuccessfully);
	}

}
