package ru.anisimov.keybattle.model.text;

import java.util.Arrays;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/7/14
 */
public class Token {
	private long position;
	private String string;

	public Token(long position, String string) {
		this.position = position;
		this.string = string;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	private Object[] keyArray() {
		return new Object[]{getPosition(), getString()};
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(keyArray());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Token)) {
			return false;
		}

		Token that = (Token) obj;
		return Arrays.equals(this.keyArray(), that.keyArray());
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("Token: ")
				.append(Arrays.toString(keyArray()))
				.toString();
	}
}
