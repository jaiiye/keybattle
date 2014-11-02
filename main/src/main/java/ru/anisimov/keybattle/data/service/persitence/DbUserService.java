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
			rs.getLong("id"),
			rs.getString("username"),
			rs.getString("password"),
			rs.getInt("locked") == 1,
			rs.getInt("enabled") == 1,
			rs.getString("roles") != null ? Arrays.stream(rs.getString("roles").split(","))
					.peek(String::trim)
					.map(role -> new SimpleGrantedAuthority(role))
					.collect(Collectors.toList()) : new ArrayList<>(),
			rs.getString("email")
	);


	private static final String ADD_USER = "insert into user(username, password, email) values (?,?,?)";
	
	private static final String ADD_USER_ROLE = "insert into user_role(user_id, role) values (?,?) " +
			" on duplicate key update modification_time = now(), enabled = 1";
	
	private static final String REMOVE_USER_ROLE = "update user_role set modification_time = now(), " +
			" enabled = 0 where user_id = ? and role = ?";

	
	private static final String LOCK_USER_BY_ID = "update user set modification_time = now(), locked = ? where id = ?";
	
	private static final String LOCK_USER_BY_NAME = "update user set modification_time = now(), locked = ? where username = ?";
	
	private static final String ENABLE_USER_BY_ID = "update user set modification_time = now(), enabled = ? where id = ?";
	
	private static final String ENABLE_USER_BY_NAME = "update user set modification_time = now(), enabled = ? where username = ?";


	private static final String SELECT_USERS = "select u.id, u.username, u.password, u.email, u.locked, u.enabled, GROUP_CONCAT(r.role) roles " +
			" from user u left join (select * from user_role where enabled = 1) r on (u.id = r.user_id) group by u.id";

	private static final String SELECT_USERS_BY_ROLE = "select u.id, u.username, u.password, u.email, u.locked, u.enabled, GROUP_CONCAT(r.role) roles " +
			" from user u left join (select * from user_role where enabled = 1) r on (u.id = r.user_id) " +
			" 	where u.id in (select distinct user_id from user_role where role = ? and enabled = 1) group by u.id";

	private static final String SELECT_USER_BY_ID = "select u.id, u.username, u.password, u.email, u.locked, u.enabled, GROUP_CONCAT(r.role) roles " +
			" from user u left join (select * from user_role where enabled = 1) r on (u.id = r.user_id) where u.id = ? group by u.id";
	
	private static final String SELECT_USER_BY_USERNAME = "select u.id, u.username, u.password, u.email, u.locked, u.enabled, GROUP_CONCAT(r.role) roles " +
			" from user u left join (select * from user_role where enabled = 1) r on (u.id = r.user_id) where u.username = ? group by u.id";

	private static final String SELECT_USER_ID_BY_USERNAME = "select id from user where username = ?";

	private static final String SELECT_USER_BY_EMAIL = "select u.id, u.username, u.password, u.email, u.locked, u.enabled, GROUP_CONCAT(r.role) roles " +
			" from user u left join (select * from user_role where enabled = 1) r on (u.id = r.user_id) where u.email = ? group by u.id";

	
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
