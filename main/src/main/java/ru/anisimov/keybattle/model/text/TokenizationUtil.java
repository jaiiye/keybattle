package ru.anisimov.keybattle.model.text;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/7/14
 */
@Component
public class TokenizationUtil {
	public List<Token> tokenizeString(String string) {
		String[] tokenStrings = string.trim().split("[\n\r ]+");
		return IntStream.range(0, tokenStrings.length).parallel()
				.mapToObj(i -> new Token(i, tokenStrings[i]))
				.filter(token -> token.getString() != null && token.getString().length() > 0)
				.collect(Collectors.toList());
	}
}
