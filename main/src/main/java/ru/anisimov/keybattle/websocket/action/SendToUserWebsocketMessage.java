package ru.anisimov.keybattle.websocket.action;

import org.apache.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.anisimov.keybattle.core.ObjectBuilder;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/28/14
 */
public class SendToUserWebsocketMessage extends AbstractSendWebsocketAction {
	private static final Logger log = Logger.getLogger(SendToUserWebsocketMessage.class);

	private String userName;

	public SendToUserWebsocketMessage(SimpMessagingTemplate simpMessagingTemplate, String destination, String userName, ObjectBuilder messageBuilder) {
		super(simpMessagingTemplate, destination, messageBuilder);
		this.userName = userName;
	}

	@Override
	public void make() {
		log.debug("Sending message to " + destination + " to user " + userName);
		simpMessagingTemplate.convertAndSendToUser(userName, destination, messageBuilder.build());
	}
}
