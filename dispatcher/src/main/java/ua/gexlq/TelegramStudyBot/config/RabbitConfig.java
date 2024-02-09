package ua.gexlq.TelegramStudyBot.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.TEXT_MESSAGE_UPDATE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.DOC_MESSAGE_UPDATE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.CALLBACKQUERY_MESSAGE_UPDATE;

import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.ANSWER_TEXT_MESSAGE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.ANSWER_EDIT_MESSAGE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.ANSWER_FORWARD_MESSAGE;
import static ua.gexlq.TelegramStudyBot.model.RabbitQueue.ANSWER_DOCUMENT_MESSAGE;

@Configuration
public class RabbitConfig {

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public Queue textMessageQueue() {
		return new Queue(TEXT_MESSAGE_UPDATE);
	}

	@Bean
	public Queue docMessageQueue() {
		return new Queue(DOC_MESSAGE_UPDATE);
	}

	@Bean
	public Queue callBackDataMessageQueue() {
		return new Queue(CALLBACKQUERY_MESSAGE_UPDATE);
	}

	@Bean
	public Queue answerMessageQueue() {
		return new Queue(ANSWER_TEXT_MESSAGE);
	}

	@Bean
	public Queue answerEditMessageQueue() {
		return new Queue(ANSWER_EDIT_MESSAGE);
	}

	@Bean
	public Queue answerForwardMessageQueue() {
		return new Queue(ANSWER_FORWARD_MESSAGE);
	}
	
	@Bean
	public Queue answerDocumentMessageQueue() {
		return new Queue(ANSWER_DOCUMENT_MESSAGE);
	}

}
