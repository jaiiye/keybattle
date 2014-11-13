package ru.anisimov.keybattle.model.tag;

import org.springframework.context.i18n.LocaleContextHolder;
import ru.anisimov.keybattle.core.HasId;
import ru.anisimov.keybattle.model.locale.Term;

import java.util.Locale;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/6/14
 */
public class Tag implements HasId {
	private long id;
	private Term name;

	public Tag() {
	}

	public Tag(long id, Term name) {
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

	public String getLocalizedName() {
		Locale locale = LocaleContextHolder.getLocale();
		return name.get(locale);
	}

	public Term getName() {
		return name;
	}

	public void setName(Term name) {
		this.name = name;
	}
}
