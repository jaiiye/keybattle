package ru.anisimov.keybattle.util.string;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/30/14
 */
public interface StringParsers {
	public static StringParser<Integer> newIntegerParser() {
		return Integer::parseInt;
	}

	public static StringParser<Long> newLongParser() {
		return Long::parseLong;
	}

	public static StringParser<Double> newDoubleParser() {
		return Double::parseDouble;
	}

	public static StringParser<Boolean> newBooleanParser() {
		return Boolean::parseBoolean;
	}

	public static StringParser<Date> newDateParser(DateFormat format) throws ParseException {
		return format::parse;
	}

	public interface StringParser<T> {
		T parse(String string) throws ParseException;
	}
}
