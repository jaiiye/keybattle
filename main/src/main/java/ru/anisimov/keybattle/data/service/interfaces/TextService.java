package ru.anisimov.keybattle.data.service.interfaces;

import ru.anisimov.keybattle.model.text.Text;

import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/7/14
 */
public interface TextService {
	Text getRandomText();

	Text getTextById(long id);

	List<Text> getAllTexts();
}
