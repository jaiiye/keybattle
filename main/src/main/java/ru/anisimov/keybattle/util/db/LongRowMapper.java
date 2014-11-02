package ru.anisimov.keybattle.util.db;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/31/14
 */
public class LongRowMapper implements RowMapper<Long> {
	public Long mapRow(final ResultSet rs, final int i) throws SQLException {
		long value = rs.getLong(1);
		if (rs.wasNull()) {
			return null;
		}
		return value;
	}
}
