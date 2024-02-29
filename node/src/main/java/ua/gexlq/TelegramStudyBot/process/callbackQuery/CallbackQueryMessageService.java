package ua.gexlq.TelegramStudyBot.process.callbackQuery;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.entity.AppUser;
import ua.gexlq.TelegramStudyBot.exceptions.CallbackQueryServiceException;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.enums.CallBackDataTypes;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.impl.ProcessDownloadFileCallBackData;
import ua.gexlq.TelegramStudyBot.process.callbackQuery.impl.ProcessDownloadSoftwareCallBackData;
import ua.gexlq.TelegramStudyBot.service.ProducerService;
import ua.gexlq.TelegramStudyBot.utils.UserInfo;
import ua.gexlq.TelegramStudyBot.utils.UserPermissionsService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CallbackQueryMessageService {

	private final ProducerService producerService;
	private final UserPermissionsService permissionsService;
	private final UserInfo userInfo;

	private final ProcessDownloadFileCallBackData processDownloadFileCallBackData;
	private final ProcessDownloadSoftwareCallBackData processDownloadSoftwareCallBackData;
	private final List<ProcessCallBackDataByState> processesState;

	public void handleUserCallBackData(Update update) {

		if (isDownloadFileCallBackData(update)) {
			handleDownloadFile(update);
		}

		else if (isDownloadSoftwareCallBackData(update)) {
			handleDownloadSoftware(update);
		}

		else {
			var response = handle(update);
			producerService.produceAnswer(response);
		}
	}

	public void handleCurrentActiveMessageId(AppUser user, Update update) {
		if (user.getCurrentActiveMessageId() != null && !user.getCurrentActiveMessageId()
				.equals(String.valueOf(update.getCallbackQuery().getMessage().getMessageId()))) {
			update.getCallbackQuery().setData(CallBackDataTypes.ERROR.toString());
		}
	}

	public void handleContactPermission(Update update) {
		if (userInfo.isUserAbleToSendFile(update.getCallbackQuery().getMessage().getChatId())) {
			permissionsService.setContactPermission(update, false);
		}
	}

	public void handleFileUploadPermission(Update update) {
		if (userInfo.isUserAbleToSendFile(update.getCallbackQuery().getMessage().getChatId())) {
			permissionsService.setFileUploadPermission(update, false);
		}
	}

	private EditMessageText handle(Update update) {
		if (isGoBackOrGoNext(update)) {
			createGoBackCallBackData(update);
		}

		for (ProcessCallBackDataByState stateProcessor : processesState) {
			if (stateProcessor.isUpdateSupportedState(update)) {
				return stateProcessor.handle(update);
			}
		}

		throw new CallbackQueryServiceException("Unsupported callBackData: " + update.getCallbackQuery().getData()
				+ "from user: " + update.getCallbackQuery().getMessage().getChatId());
	}

	private boolean isGoBackOrGoNext(Update update) {
		String callBackData = update.getCallbackQuery().getData();
		return callBackData.contains(CallBackDataTypes.GO_BACK_TO.toString())
				|| callBackData.contains(CallBackDataTypes.GO_NEXT_TO.toString());
	}

	private boolean isDownloadFileCallBackData(Update update) {
		return processDownloadFileCallBackData.isUpdateSupportedState(update);
	}

	private boolean isDownloadSoftwareCallBackData(Update update) {
		return processDownloadSoftwareCallBackData.isUpdateSupportedState(update);
	}

	private void createGoBackCallBackData(Update update) {
		var callBackQuery = update.getCallbackQuery();
		String newCallBackData = callBackQuery.getData().substring(callBackQuery.getData().indexOf(">") + 1);
		callBackQuery.setData(newCallBackData);
	}

	private void handleDownloadFile(Update update) {
		var forwardMessage = processDownloadFileCallBackData.handle(update);
		producerService.produceAnswer(forwardMessage);
	}

	private void handleDownloadSoftware(Update update) {
		var forwardMessage = processDownloadSoftwareCallBackData.handle(update);
		producerService.produceAnswer(forwardMessage);
	}
}
