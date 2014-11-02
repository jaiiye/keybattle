package ru.anisimov.keybattle.util.db;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/2/14
 */
public class ByteArrayRowMapper implements RowMapper<byte[]> {
	public byte[] mapRow(final ResultSet rs, final int i) throws SQLException {
		byte[] value = rs.getBytes(1);
		if (rs.wasNull()) {
			return null;
		}
		return value;
	}
}
