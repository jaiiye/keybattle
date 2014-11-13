package ru.anisimov.keybattle.data.service.persitence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.anisimov.keybattle.model.history.UserHistoryRecord;
import ru.anisimov.keybattle.data.service.interfaces.UserHistoryService;
import ru.anisimov.keybattle.data.service.interfaces.UserService;

import java.util.List;

import static java.util.Collections.EMPTY_LIST;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/2/14
 */
@Service("userHistoryService")
public class DbUserHistoryService implements UserHistoryService {
	private static final RowMapper<UserHistoryRecord> HISTORY_RECORD_ROW_MAPPER = (rs, rowNum) -> {
		UserHistoryRecord result = new UserHistoryRecord();
		result.setId(rs.getLong("ID"));
		result.setUserId(rs.getLong("USER_ID"));
		result.setActionId(rs.getInt("HISTORY_ACTION_ID"));
		result.setActorId(rs.getInt("ACTOR_ID"));
		result.setCreationTime(rs.getTimestamp("CREATION_TIME").toLocalDateTime());
		result.setActionName(rs.getString("NAME"));
		result.setActionDescription(rs.getString("DESCRIPTION"));
		return result;
	};

	private static final String SELECT_RECORDS_BY_ACTOR_ID = "SELECT H.*, A.NAME, A.DESCRIPTION FROM USER_HISTORY H " +
			" LEFT JOIN HISTORY_ACTION A ON (H.HISTORY_ACTION_ID = A.ID) WHERE H.ACTOR_ID = ? " +
			" ORDER BY H.CREATION_TIME";

	private static final String SELECT_RECORDS_BY_USER_ID = "SELECT H.*, A.NAME, A.DESCRIPTION FROM USER_HISTORY H " +
			" LEFT JOIN HISTORY_ACTION A ON (H.HISTORY_ACTION_ID = A.ID) WHERE H.USER_ID = ? " +
			" ORDER BY H.CREATION_TIME";

	private static final String SELECT_RECORDS_BY_USER_ID_ACTION_ID = "SELECT H.*, A.NAME, A.DESCRIPTION FROM USER_HISTORY H " +
			" LEFT JOIN HISTORY_ACTION A ON (H.HISTORY_ACTION_ID = A.ID) WHERE H.USER_ID = ? AND A.ID = ?" +
			" ORDER BY H.CREATION_TIME";
	private static final String ADD_RECORD = "INSERT INTO USER_HISTORY (USER_ID, HISTORY_ACTION_ID, ACTOR_ID) " +
			" VALUES (?, ?, ?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private UserService userService;
	
	@Override
	public boolean addHistoryRecord(long userId, int actionId, long actorId) {
		return jdbcTemplate.update(ADD_RECORD, userId, actionId, actorId) != 0;
	}

	@Override
	public boolean addHistoryRecord(String userName, int actionId, long actorId) {
		Long userId = userService.getUserIdByUserName(userName);
		return userId == null ? false : addHistoryRecord(userId, actionId, actorId);
	}

	@Override
	public List<UserHistoryRecord> getRecordsByUserAndAction(long userId, int actionId) {
		List<UserHistoryRecord> records = jdbcTemplate.query(SELECT_RECORDS_BY_USER_ID_ACTION_ID,
				HISTORY_RECORD_ROW_MAPPER, userId, actionId);
		return records == null ? EMPTY_LIST : records;
	}

	@Override
	public List<UserHistoryRecord> getRecordsByUserAndAction(String userName, int actionId) {
		Long userId = userService.getUserIdByUserName(userName);
		return userId == null ? EMPTY_LIST : getRecordsByUserAndAction(userId, actionId);
	}

	@Override
	public List<UserHistoryRecord> getRecordsByUser(long userId) {
		List<UserHistoryRecord> records = jdbcTemplate.query(SELECT_RECORDS_BY_USER_ID,
				HISTORY_RECORD_ROW_MAPPER, userId);
		return records == null ? EMPTY_LIST : records;
	}

	@Override
	public List<UserHistoryRecord> getRecordsByUser(String userName) {
		Long userId = userService.getUserIdByUserName(userName);
		return userId == null ? EMPTY_LIST : getRecordsByUser(userId);
	}

	@Override
	public List<UserHistoryRecord> getRecordsByActor(long actorId) {
		List<UserHistoryRecord> records = jdbcTemplate.query(SELECT_RECORDS_BY_ACTOR_ID, 
				HISTORY_RECORD_ROW_MAPPER, actorId);
		return records == null ? EMPTY_LIST : records;
	}
}
