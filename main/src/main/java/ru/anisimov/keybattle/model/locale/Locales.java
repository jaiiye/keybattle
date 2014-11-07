package ru.anisimov.keybattle.model.locale;


import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/7/14
 */
public interface Locales {
	Locale RU = StringUtils.parseLocaleString("ru");
	Locale EN = StringUtils.parseLocaleString("en");
	
	Locale DEFAULT = EN;
}
