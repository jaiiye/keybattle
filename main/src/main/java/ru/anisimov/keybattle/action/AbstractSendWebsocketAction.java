package ru.anisimov.keybattle.action;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.anisimov.keybattle.core.Action;
import ru.anisimov.keybattle.core.ObjectBuilder;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/28/14
 */
public abstract class AbstractSendWebsocketAction implements Action {
	protected SimpMessagingTemplate simpMessagingTemplate;
	protected String destination;
	protected ObjectBuilder messageBuilder;

	public AbstractSendWebsocketAction(SimpMessagingTemplate simpMessagingTemplate, String destination, ObjectBuilder messageBuilder) {
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.destination = destination;
		this.messageBuilder = messageBuilder;
	}
}

