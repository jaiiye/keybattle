package ru.anisimov.keybattle.data.service.persitence;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anisimov.keybattle.model.user.User;
import ru.anisimov.keybattle.data.service.interfaces.UserService;
import ru.anisimov.keybattle.util.db.LongRowMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.EMPTY_LIST;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/15/14
 */
@Service("userService")
public class DbUserService implements UserService {
	private static final Logger log = Logger.getLogger(DbUserService.class);

	private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(
			rs.getLong("ID"),
			rs.getString("USERNAME"),
			rs.getString("PASSWORD"),
			rs.getInt("LOCKED") == 1,
			rs.getInt("ENABLED") == 1,
			rs.getString("ROLES") != null ? Arrays.stream(rs.getString("ROLES").split(","))
					.peek(String::trim)
					.map(role -> new SimpleGrantedAuthority(role))
					.collect(Collectors.toList()) : new ArrayList<>(),
			rs.getString("EMAIL")
	);


	private static final String ADD_USER = "INSERT INTO USER(USERNAME, PASSWORD, EMAIL) VALUES (?,?,?)";
	
	private static final String ADD_USER_ROLE = "INSERT INTO USER_ROLE(USER_ID, ROLE) VALUES (?,?) " +
			" ON DUPLICATE KEY UPDATE MODIFICATION_TIME = NOW(), ENABLED = 1";
	
	private static final String REMOVE_USER_ROLE = "UPDATE USER_ROLE SET MODIFICATION_TIME = NOW(), " +
			" ENABLED = 0 WHERE USER_ID = ? AND ROLE = ?";

	
	private static final String LOCK_USER_BY_ID = "UPDATE USER SET MODIFICATION_TIME = NOW(), LOCKED = ? WHERE ID = ?";
	
	private static final String LOCK_USER_BY_NAME = "UPDATE USER SET MODIFICATION_TIME = NOW(), LOCKED = ? WHERE USERNAME = ?";
	
	private static final String ENABLE_USER_BY_ID = "UPDATE USER SET MODIFICATION_TIME = NOW(), ENABLED = ? WHERE ID = ?";
	
	private static final String ENABLE_USER_BY_NAME = "UPDATE USER SET MODIFICATION_TIME = NOW(), ENABLED = ? WHERE USERNAME = ?";


	private static final String SELECT_USERS = "SELECT U.ID, U.USERNAME, U.PASSWORD, U.EMAIL, U.LOCKED, U.ENABLED, GROUP_CONCAT(R.ROLE) ROLES " +
			" FROM USER U LEFT JOIN (SELECT * FROM USER_ROLE WHERE ENABLED = 1) R ON (U.ID = R.USER_ID) GROUP BY U.ID";

	private static final String SELECT_USERS_BY_ROLE = "SELECT U.ID, U.USERNAME, U.PASSWORD, U.EMAIL, U.LOCKED, U.ENABLED, GROUP_CONCAT(R.ROLE) ROLES " +
			" FROM USER U LEFT JOIN (SELECT * FROM USER_ROLE WHERE ENABLED = 1) R ON (U.ID = R.USER_ID) " +
			" 	WHERE U.ID IN (SELECT DISTINCT USER_ID FROM USER_ROLE WHERE ROLE = ? AND ENABLED = 1) GROUP BY U.ID";

	private static final String SELECT_USER_BY_ID = "SELECT U.ID, U.USERNAME, U.PASSWORD, U.EMAIL, U.LOCKED, U.ENABLED, GROUP_CONCAT(R.ROLE) ROLES " +
			" FROM USER U LEFT JOIN (SELECT * FROM USER_ROLE WHERE ENABLED = 1) R ON (U.ID = R.USER_ID) WHERE U.ID = ? GROUP BY U.ID";
	
	private static final String SELECT_USER_BY_USERNAME = "SELECT U.ID, U.USERNAME, U.PASSWORD, U.EMAIL, U.LOCKED, U.ENABLED, GROUP_CONCAT(R.ROLE) ROLES " +
			" FROM USER U LEFT JOIN (SELECT * FROM USER_ROLE WHERE ENABLED = 1) R ON (U.ID = R.USER_ID) WHERE U.USERNAME = ? GROUP BY U.ID";

	private static final String SELECT_USER_ID_BY_USERNAME = "SELECT ID FROM USER WHERE USERNAME = ?";

	private static final String SELECT_USER_BY_EMAIL = "SELECT U.ID, U.USERNAME, U.PASSWORD, U.EMAIL, U.LOCKED, U.ENABLED, GROUP_CONCAT(R.ROLE) ROLES " +
			" FROM USER U LEFT JOIN (SELECT * FROM USER_ROLE WHERE ENABLED = 1) R ON (U.ID = R.USER_ID) WHERE U.EMAIL = ? GROUP BY U.ID";

	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	@Override
	public boolean addUser(User user) {		
		String userName = user.getUsername();

		int userAdded = jdbcTemplate.update(ADD_USER, userName, user.getPassword(), user.getEmail());
			
		if (userAdded != 1) {
			log.error(String.format("Could not insert user %s into db", user));
			return false;
		}
	
		final long userId = getUserIdByUserName(userName);
		final List<String> roles = user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		int[] rolesAdded = jdbcTemplate.batchUpdate(ADD_USER_ROLE, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
				preparedStatement.setLong(1, userId);
				preparedStatement.setString(2, roles.get(i));
			}

			@Override
			public int getBatchSize() {
				return user.getAuthorities() != null ? user.getAuthorities().size() : 0;
			}
		});
		int rolesAddedTotal = 0;
		for (int i = 0; i < rolesAdded.length; i++) {
			rolesAddedTotal += rolesAdded[i];
		}
		if (rolesAddedTotal != roles.size()) {
			log.error(String.format("Could not insert user roles %s into db", String.valueOf(roles)));
			return false;
		}
		
		return true;
	}

	@Override
	public boolean removeUser(long userId) {
		return enableUser(userId, false);
	}

	@Override
	public boolean removeUser(String userName) {
		return enableUser(userName, false);
	}

	@Override
	public boolean addRole(long userId, String role) {
		return jdbcTemplate.update(ADD_USER_ROLE, userId, role) == 1;
	}
	
	@Override
	public boolean addRole(String userName, String role) {
		return addRole(getUserIdByUserName(userName), role);
	}

	@Override
	public boolean removeRole(long userId, String role) {
		return jdbcTemplate.update(REMOVE_USER_ROLE, userId, role) == 1;
	}

	@Override
	public boolean removeRole(String userName, String role) {
		return removeRole(getUserIdByUserName(userName), role);
	}

	@Override
	public boolean lockUser(long userId, boolean locked) {
		return jdbcTemplate.update(LOCK_USER_BY_ID, locked ? 1: 0, userId) == 1;
	}

	@Override
	public boolean lockUser(String userName, boolean locked) {
		return jdbcTemplate.update(LOCK_USER_BY_NAME, locked ? 1: 0, userName) == 1;
	}

	@Override
	public boolean enableUser(long userId, boolean enabled) {
		return jdbcTemplate.update(ENABLE_USER_BY_ID, enabled ? 1: 0, userId) == 1;
	}

	@Override
	public boolean enableUser(String userName, boolean enabled) {
		return jdbcTemplate.update(ENABLE_USER_BY_NAME, enabled ? 1: 0, userName) == 1;
	}

	@Override
	public List<User> getUsers(String role) {
		List<User> users = jdbcTemplate.query(SELECT_USERS_BY_ROLE, USER_ROW_MAPPER, role);
		return users == null ? EMPTY_LIST : users;
	}

	@Override
	public List<User> getUsers() {
		List<User> users = jdbcTemplate.query(SELECT_USERS, USER_ROW_MAPPER);
		return users == null ? EMPTY_LIST : users;
	}

	@Override
	public User getUserById(long userId) {
		List<User> users = jdbcTemplate.query(SELECT_USER_BY_ID, USER_ROW_MAPPER, userId);
		return users != null && users.size() > 0 ? users.get(0) : null;
	}

	@Override
	public User getUserByUserName(String userName) {
		List<User> users = jdbcTemplate.query(SELECT_USER_BY_USERNAME, USER_ROW_MAPPER, userName);
		return users != null && users.size() > 0 ? users.get(0) : null;
	}

	@Override
	public Long getUserIdByUserName(String userName) {
		List<Long> users = jdbcTemplate.query(SELECT_USER_ID_BY_USERNAME, new LongRowMapper(), userName);
		return users != null && users.size() > 0 ? users.get(0) : null;
	}

	@Override
	public User getUserByEmail(String email) {
		List<User> users = jdbcTemplate.query(SELECT_USER_BY_EMAIL, USER_ROW_MAPPER, email);
		return users != null && users.size() > 0 ? users.get(0) : null;
	}
}
