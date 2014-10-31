package ru.anisimov.keybattle.model.chat;

import java.util.Date;
import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/23/14
 */
public class MultiChatEvent extends TimeEvent {
	private List<ChatEvent> events;

	public MultiChatEvent(Date eventtime, TimeEventType type, List<ChatEvent> events) {
		super(eventtime, type);
		this.events = events;
	}

	public List<ChatEvent> getEvents() {
		return events;
	}
}
