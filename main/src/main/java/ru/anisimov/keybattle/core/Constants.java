package ru.anisimov.keybattle.core;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/2/14
 */
public interface Constants<T> {
	static <T> String getNameById(T object, Class<? extends Constants<T>> currentClass) {
		List<String> fieldNames = Arrays.asList(currentClass.getFields()).stream()
				.map(field -> {
					try {
						return Objects.equals(field.get(currentClass), object) ? field.getName() : null;
					} catch (IllegalAccessException e) {
						return null;
					}
				})
				.filter(field -> field != null).collect(Collectors.toList());
		if (fieldNames == null || fieldNames.size() == 0) {
			return null;
		}
		return fieldNames.get(0);
	}
}
