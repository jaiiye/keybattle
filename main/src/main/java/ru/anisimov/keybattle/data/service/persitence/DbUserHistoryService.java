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
		result.setId(rs.getLong("id"));
		result.setUserId(rs.getLong("user_id"));
		result.setActionId(rs.getInt("history_action_id"));
		result.setActorId(rs.getInt("actor_id"));
		result.setCreationTime(rs.getTimestamp("creation_time").toLocalDateTime());
		result.setActionName(rs.getString("name"));
		result.setActionDescription(rs.getString("description"));
		return result;
	};

	private static final String SELECT_RECORDS_BY_ACTOR_ID = "select h.*, a.name, a.description from user_history h " +
			" left join history_action a on (h.history_action_id = a.id) where h.actor_id = ? " +
			" order by h.creation_time";

	private static final String SELECT_RECORDS_BY_USER_ID = "select h.*, a.name, a.description from user_history h " +
			" left join history_action a on (h.history_action_id = a.id) where h.user_id = ? " +
			" order by h.creation_time";

	private static final String SELECT_RECORDS_BY_USER_ID_ACTION_ID = "select h.*, a.name, a.description from user_history h " +
			" left join history_action a on (h.history_action_id = a.id) where h.user_id = ? and a.id = ?" +
			" order by h.creation_time";
	private static final String ADD_RECORD = "insert into user_history (user_id, history_action_id, actor_id) " +
			" values (?, ?, ?)";

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
