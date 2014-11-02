package ru.anisimov.keybattle.model.text;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.anisimov.keybattle.data.service.interfaces.TextService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/7/14
 */
@Component
public class TokenizedTextManager {
	@Autowired
	private TokenizationUtil tokenizationUtil;
	@Autowired
	private TextService textService;

	public TokenizedText getRandomText() {
		return tokenizeText(textService.getRandomText());
	}

	public TokenizedText getTextById(long id) {
		return tokenizeText(textService.getTextById(id));
	}

	public List<TokenizedText> getAllTexts() {
		return textService.getAllTexts().stream().parallel()
				.map(text -> tokenizeText(text))
				.collect(Collectors.toList());
	}

	private TokenizedText tokenizeText(Text text) {
		return new TokenizedText(text.getId(), tokenizationUtil.tokenizeString(text.getString()));
	}
}
