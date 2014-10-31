package ru.anisimov.keybattle.model.chat;

import java.util.Date;
import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/22/14
 */
public class UserEvent extends TimeEvent {
	private List<String> userNames;

	public UserEvent(Date eventtime, TimeEventType type, List<String> userNames) {
		super(eventtime, type);
		this.userNames = userNames;
	}

	public List<String> getUserNames() {
		return userNames;
	}
}
