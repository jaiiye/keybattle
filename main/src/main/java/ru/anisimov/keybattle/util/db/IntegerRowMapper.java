package ru.anisimov.keybattle.util.db;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/31/14
 */
public class IntegerRowMapper implements RowMapper<Integer> {
	public Integer mapRow(final ResultSet rs, final int i) throws SQLException {
		int value = rs.getInt(1);
		if (rs.wasNull()) {
			return null;
		}
		return value;
	}
}
