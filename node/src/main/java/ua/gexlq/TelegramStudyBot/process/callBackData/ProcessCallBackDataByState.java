package ua.gexlq.TelegramStudyBot.process.callBackData;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ProcessCallBackDataByState {
	public EditMessageText handle(Update update);

	public boolean isUpdateSupportedState(Update update);
}
