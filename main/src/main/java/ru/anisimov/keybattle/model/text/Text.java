package ru.anisimov.keybattle.model.text;

import ru.anisimov.keybattle.core.HasId;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/7/14
 */
public class Text implements HasId {
	public static final Text NULL_TEXT = new Text(-1, "NULL TEXT");

	private long id;
	private String string;

	public Text(long id, String string) {
		this.id = id;
		this.string = string;
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}
