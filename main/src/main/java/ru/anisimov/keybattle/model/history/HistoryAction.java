package ru.anisimov.keybattle.model.history;

import ru.anisimov.keybattle.core.Constants;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/31/14
 */
public interface HistoryAction extends Constants<Integer> {
	int USER_ADDED = 0;
	int USER_ROLE_ADDED = 1;
	int USER_ROLE_REMOVED = 2;
	int USER_LOCKED = 3;
	int USER_UNLOCKED = 4;
	int USER_DISABLED = 5;
	int USER_ENABLED = 6;

	int USER_INFO_ADDED = 7;
	int USER_INFO_CHANGED = 8;
	int USER_INFO_HIDDEN = 9;
	int USER_INFO_SHOWN = 10;
	
	static String getNameById(Integer id) {
		return Constants.getNameById(id, HistoryAction.class);
	}
}
