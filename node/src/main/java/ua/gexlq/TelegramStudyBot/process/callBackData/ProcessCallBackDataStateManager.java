package ua.gexlq.TelegramStudyBot.process.callBackData;

import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.process.callBackData.enums.CallBackDataTypes;

@RequiredArgsConstructor
@Component
public class ProcessCallBackDataStateManager {

	private final List<ProcessCallBackDataByState> processesState;

	public void createGoBackCallBackData(Update update) {
		var callBackQuery = update.getCallbackQuery();
		String newCallBackData = callBackQuery.getData().substring(callBackQuery.getData().indexOf(">") + 1);
		callBackQuery.setData(newCallBackData);
	}

	public EditMessageText handle(Update update) {

		if (update.getCallbackQuery().getData().contains(CallBackDataTypes.GO_BACK_TO.toString())
				|| update.getCallbackQuery().getData().contains(CallBackDataTypes.GO_NEXT_TO.toString()))
			createGoBackCallBackData(update);

		for (ProcessCallBackDataByState stateProcessor : processesState) {
			if (stateProcessor.isUpdateSupportedState(update)) {
				return stateProcessor.handle(update);
			}
		}
		return null;
	}

}
