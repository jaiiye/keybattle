package ru.anisimov.keybattle.model.chat;

import java.util.Date;
import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/20/14
 */
public class ChatEvent extends TimeEvent {
	private List<ChatMessage> messages;

	public ChatEvent(Date eventtime, TimeEventType type, List<ChatMessage> messages) {
		super(eventtime, type);
		this.messages = messages;
	}

	public List<ChatMessage> getMessages() {
		return messages;
	}
}
