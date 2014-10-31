package ru.anisimov.keybattle.model.text;

import ru.anisimov.keybattle.core.HasId;

import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/7/14
 */
public class TokenizedText implements HasId {
	private long id;
	private List<Token> tokens;

	public TokenizedText(long id, List<Token> tokens) {
		this.id = id;
		this.tokens = tokens;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
