package ru.anisimov.keybattle.service.persitence;

import ru.anisimov.keybattle.model.text.Text;
import ru.anisimov.keybattle.service.interfaces.TextService;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/7/14
 */
public class MemoryTextService implements TextService {
	private static final Random RND = new Random(System.currentTimeMillis());

	private List<Text> texts;

	public MemoryTextService(List<Text> texts) {
		this.texts = Collections.unmodifiableList(texts);
	}

	@Override
	public Text getRandomText() {
		return texts.size() > 0
				? texts.get(RND.nextInt(texts.size()))
				: Text.NULL_TEXT;
	}

	@Override
	public Text getTextById(long id) {
		return texts.stream().parallel()
				.filter(text -> text.getId() == id)
				.findFirst().orElse(Text.NULL_TEXT);
	}

	@Override
	public List<Text> getAllTexts() {
		return texts;
	}
}
