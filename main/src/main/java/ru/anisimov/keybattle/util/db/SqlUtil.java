package ru.anisimov.keybattle.util.db;

import java.util.Arrays;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/10/14
 */
public class SqlUtil {
	public static String inBlock(String... strings) {
		StringBuilder result = new StringBuilder()
				.append("(");
		Arrays.stream(strings).forEach(string -> result
				.append("'")
				.append(string)
				.append("',")
		);
		result.append(")");
		return result.toString();
	}
}
