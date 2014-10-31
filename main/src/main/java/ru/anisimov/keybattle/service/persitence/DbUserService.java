package ru.anisimov.keybattle.service.persitence;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.anisimov.keybattle.model.user.KeybattleUser;
import ru.anisimov.keybattle.service.interfaces.UserService;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/15/14
 */
@Service("userService")
public class DbUserService implements UserService {
	private static final Logger log = Logger.getLogger(DbUserService.class);

	private static final RowMapper<KeybattleUser> USER_ROW_MAPPER = (resultSet, i) -> new KeybattleUser(
			resultSet.getLong("id"),
			resultSet.getString("username"),
			resultSet.getString("password"),
			resultSet.getInt("enabled") == 1,
			resultSet.getString("roles") != null ? Arrays.stream(resultSet.getString("roles").split(","))
					.peek(String::trim)
					.map(role -> new SimpleGrantedAuthority(role))
					.collect(Collectors.toList()) : new ArrayList<>(),
			resultSet.getString("email")
	);

	private static final String SELECT_USERS = "select u.id, u.username, u.password, u.enabled, GROUP_CONCAT(r.role) roles, u.email " +
			"from user u left join user_role r on (u.id = r.user_id) where u.enabled = 1 group by u.id";

	private static final String SELECT_USER_BY_USERNAME = "select u.id, u.username, u.password, u.enabled, GROUP_CONCAT(r.role) roles, u.email " +
			"from user u left join user_role r on (u.id = r.user_id) where u.username = ? group by u.id";

	private static final String SELECT_USER_BY_EMAIL = "select u.id, u.username, u.password, u.enabled, GROUP_CONCAT(r.role) roles, u.email " +
			"from user u left join user_role r on (u.id = r.user_id) where u.email = ? group by u.id";

	private static final String INSERT_USER = "insert into user(username, password, email, enabled) values (?,?,?,?)";
	private static final String INSERT_USER_ROLE = "insert into user_role(user_id, role) values (?,?)";

	private static final String DELETE_USER = "update user set enabled = 0 where username = ?";

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public boolean addUser(KeybattleUser user) {
		return transactionTemplate.execute(transactionStatus -> {
			if (getByUserName(user.getUsername()) == null) {
				log.info("Registering new user " + user);

				jdbcTemplate.update(INSERT_USER, user.getUsername(), user.getPassword(), user.getEmail(), user.isEnabled() ? 1 : 0);
				final KeybattleUser existingUser = getByUserName(user.getUsername());
				final List<String> roles = user.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList());

				jdbcTemplate.batchUpdate(INSERT_USER_ROLE, new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
						preparedStatement.setLong(1, existingUser.getId());
						preparedStatement.setString(2, roles.get(i));
					}

					@Override
					public int getBatchSize() {
						return user.getAuthorities() != null ? user.getAuthorities().size() : 0;
					}
				});
				return true;
			}
			return false;
		});
	}

	@Override
	public boolean deleteUser(KeybattleUser user) {
		return deleteUser(user.getUsername());
	}

	@Override
	public boolean deleteUser(String userName) {
		return transactionTemplate.execute(transactionStatus -> {
			if (getByUserName(userName) != null) {
				jdbcTemplate.update(DELETE_USER, userName);
				return true;
			}
			return false;
		});
	}

	@Override
	public List<KeybattleUser> getUsers() {
		return jdbcTemplate.query(SELECT_USERS, USER_ROW_MAPPER);
	}

	@Override
	public KeybattleUser getByUserName(String userName) {
		List<KeybattleUser> users = jdbcTemplate.query(SELECT_USER_BY_USERNAME, USER_ROW_MAPPER, userName);
		return users != null && users.size() > 0 ? users.get(0) : null;
	}

	@Override
	public KeybattleUser getByEmail(String email) {
		List<KeybattleUser> users = jdbcTemplate.query(SELECT_USER_BY_EMAIL, USER_ROW_MAPPER, email);
		return users != null && users.size() > 0 ? users.get(0) : null;
	}
}
