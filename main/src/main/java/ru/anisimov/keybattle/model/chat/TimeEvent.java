package ru.anisimov.keybattle.model.chat;

import java.util.Date;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/23/14
 */
public class TimeEvent {
	private Date eventtime;
	private TimeEventType type;

	public TimeEvent(Date eventtime, TimeEventType type) {
		this.eventtime = eventtime;
		this.type = type;
	}

	public Date getEventtime() {
		return eventtime;
	}

	public TimeEventType getType() {
		return type;
	}
}
