package ru.anisimov.keybattle.action;

import org.apache.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.anisimov.keybattle.core.ObjectBuilder;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/22/14
 */
public class SendWebsocketMessage extends AbstractSendWebsocketAction {
	private static final Logger log = Logger.getLogger(SendWebsocketMessage.class);

	public SendWebsocketMessage(SimpMessagingTemplate simpMessagingTemplate, String destination, ObjectBuilder messageBuilder) {
		super(simpMessagingTemplate, destination, messageBuilder);
	}

	@Override
	public void make() {
		log.debug("Sending message to " + destination);
		simpMessagingTemplate.convertAndSend(destination, messageBuilder.build());
	}
}
