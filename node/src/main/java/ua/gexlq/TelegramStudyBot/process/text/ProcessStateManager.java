package ua.gexlq.TelegramStudyBot.process.text;

import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;
import ua.gexlq.TelegramStudyBot.entity.enums.UserState;

@RequiredArgsConstructor
@Component
public class ProcessStateManager {

	private final List<ProcessMessageByUserState> processesState;

	public SendMessage handle(UserState currentUserState, Update udpate) {
		for (ProcessMessageByUserState stateProcessor : processesState) {
			if (stateProcessor.getSupportedState().equals(currentUserState)) {
				return stateProcessor.handle(udpate);
			}
		}
		return null;
	}

}
