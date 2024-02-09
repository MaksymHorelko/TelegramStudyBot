package ua.gexlq.TelegramStudyBot.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MessageUtils {
	private final MessageLoader loader;

	private final UserInfo userInfo;

	public ForwardMessage createForwardMessageDocument(String fromChatId, String toChatId, Integer messageIdToForward) {
		var forwardMessage = new ForwardMessage();
		forwardMessage.setFromChatId(fromChatId);
		forwardMessage.setChatId(toChatId);
		forwardMessage.setMessageId(messageIdToForward);

		return forwardMessage;
	}

	public EditMessageText createEditMessageWithAnswerCode(Update update, String messageCode) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		long messageId = update.getCallbackQuery().getMessage().getMessageId();

		var editMessage = new EditMessageText();
		editMessage.setChatId(String.valueOf(chatId));
		editMessage.setMessageId((int) messageId);
		editMessage.setText(getAnswerTextByCode(messageCode, userInfo.getUserLanguage(chatId)));
		return editMessage;
	}

	public EditMessageText createEditMessageWithAnswerCode(long chatId, long messageId, String messageCode) {
		var editMessage = new EditMessageText();
		editMessage.setChatId(String.valueOf(chatId));
		editMessage.setMessageId((int) messageId);
		editMessage.setText(getAnswerTextByCode(messageCode, userInfo.getUserLanguage(chatId)));
		return editMessage;
	}

	public EditMessageText createEditMessageWithText(Update update, String text) {
		long chatId = update.getCallbackQuery().getMessage().getChatId();
		long messageId = update.getCallbackQuery().getMessage().getMessageId();

		var editMessage = new EditMessageText();
		editMessage.setChatId(String.valueOf(chatId));
		editMessage.setMessageId((int) messageId);
		editMessage.setText(text);
		return editMessage;
	}

	public EditMessageText createEditMessageWithText(long chatId, long messageId, String text) {
		var editMessage = new EditMessageText();
		editMessage.setChatId(String.valueOf(chatId));
		editMessage.setMessageId((int) messageId);
		editMessage.setText(text);
		return editMessage;
	}

	public SendMessage createSendMessageWithText(Update update, String text) {
		long chatId = update.getMessage().getChatId();
		var sendMessage = new SendMessage();
		sendMessage.setChatId(String.valueOf(chatId));
		sendMessage.setText(text);
		return sendMessage;
	}

	public SendMessage createSendMessageWithText(long chatId, String text) {
		var sendMessage = new SendMessage();
		sendMessage.setChatId(String.valueOf(chatId));
		sendMessage.setText(text);
		return sendMessage;
	}

	public SendMessage createSendMessageWithAnswerCode(Update update, String messageCode) {
		long chatId = update.getMessage().getChatId();
		var sendMessage = new SendMessage();
		sendMessage.setChatId(String.valueOf(chatId));

		sendMessage.setText(getAnswerTextByCode(messageCode, userInfo.getUserLanguage(chatId)));
		return sendMessage;
	}

	public String getAnswerTextByCode(String messageCode, String language) {
		return loader.getTextByCode(messageCode, language);
	}
}
