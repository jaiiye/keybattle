package ru.anisimov.keybattle.model.locale;

import ru.anisimov.keybattle.core.HasId;

import java.util.HashMap;

/**
 * Text by locale
 * 
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/7/14
 */
public class Term extends HashMap<String, String> implements HasId {
	private long id;
	
	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
