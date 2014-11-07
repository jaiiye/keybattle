package ru.anisimov.keybattle.model.tag;

import ru.anisimov.keybattle.core.HasId;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/6/14
 */
public class Tag implements HasId {
	private long id;
	private String name;

	public Tag() {
	}

	public Tag(long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
