package ru.anisimov.keybattle.data.service.persitence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.anisimov.keybattle.model.user.Gender;
import ru.anisimov.keybattle.model.user.UserInfo;
import ru.anisimov.keybattle.data.service.interfaces.UserInfoService;
import ru.anisimov.keybattle.data.service.interfaces.UserService;
import ru.anisimov.keybattle.util.db.ByteArrayRowMapper;

import java.sql.Date;
import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/1/14
 */
@Service("userInfoService")
public class DbUserInfoService implements UserInfoService {
	private static final RowMapper<UserInfo> USER_INFO_ROW_MAPPER = (rs, rowNum) -> {
		UserInfo result = new UserInfo();
		result.setUserId(rs.getLong("user_id"));
		result.setName(rs.getString("name"));
		int age = rs.getInt("age");
		result.setAge(age == 0 ? null : age);
		result.setGender(Gender.values()[rs.getInt("gender")]);
		Date date = rs.getDate("dob");
		result.setDateOfBirth(date == null ? null : date.toLocalDate());
		result.setCountry(rs.getString("country"));
		result.setStatus(rs.getString("status"));
		result.setAvatar(rs.getBytes("avatar"));
		result.setHidden(rs.getInt("hidden") == 1);
		return result;
	};

	private static final String SELECT_INFO_BY_USER_ID = "select * from v_user_info where user_id = ?";

	private static final String SELECT_INFO_WITHOUT_AVATAR_BY_USER_ID = "select user_id, name, age, gender, dob, " +
			" country, status, null avatar, hidden from v_user_info where user_id = ?";

	private static final String SELECT_AVATAR_BY_USER_ID = "select avatar from v_user_info where user_id = ?";

	private static final String SHOW_USER_INFO_BY_USER_ID = "update user_info set modification_time = now(), " +
			" hidden = ? where user_id = ?";

	private static final String CHANGE_USER_AVATAR_BY_USER_ID = "update user_info set modification_time = now(), " +
			" avatar = ? where user_id = ?";

	private static final String CHANGE_USER_INFO_BY_USER_ID = "update user_info set modification_time = now(), " +
			" name = ?, gender = ?, dob = ?, country = ?, status = ?, hidden = ? where user_id = ?";

	private static final String ADD_INITIAL_INFO = "insert into user_info (user_id) values (?)";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private UserService userService;
	
	@Override
	public boolean addInitialUserInfo(long userId) {
		return jdbcTemplate.update(ADD_INITIAL_INFO, userId) != 0;
	}

	@Override
	public boolean addInitialUserInfo(String userName) {
		Long userId = userService.getUserIdByUserName(userName);
		return userId == null ? false : addInitialUserInfo(userId);
	}

	@Override
	public boolean changeUserInfoWithoutAvatar(long userId, UserInfo userInfo) {
		return jdbcTemplate.update(CHANGE_USER_INFO_BY_USER_ID,
				userInfo.getName(), userInfo.getGender().ordinal(), Date.valueOf(userInfo.getDateOfBirth()), 
				userInfo.getCountry(), userInfo.getStatus(), userInfo.isHidden() ? 1 : 0, userId) != 0;
	}

	@Override
	public boolean changeUserInfoWithoutAvatar(String userName, UserInfo userInfo) {
		Long userId = userService.getUserIdByUserName(userName);
		return userId == null ? false : changeUserInfoWithoutAvatar(userId, userInfo);
	}

	@Override
	public boolean changeUserAvatar(long userId, byte[] avatar) {
		return jdbcTemplate.update(CHANGE_USER_AVATAR_BY_USER_ID, 
				avatar, userId) != 0;
	}

	@Override
	public boolean changeUserAvatar(String userName, byte[] avatar) {
		Long userId = userService.getUserIdByUserName(userName);
		return userId == null ? false : changeUserAvatar(userId, avatar);
	}

	@Override
	public boolean showUserInfo(long userId, boolean visible) {
		return jdbcTemplate.update(SHOW_USER_INFO_BY_USER_ID, 
				visible ? 0: 1, userId) != 0;
	}

	@Override
	public boolean showUserInfo(String userName, boolean visible) {
		Long userId = userService.getUserIdByUserName(userName);
		return userId == null ? false : showUserInfo(userId, visible);
	}

	@Override
	public UserInfo getUserInfo(long userId) {
		List<UserInfo> infos = jdbcTemplate.query(SELECT_INFO_BY_USER_ID, 
				USER_INFO_ROW_MAPPER, userId);
		return infos == null || infos.size() == 0 ? null : infos.get(0);
	}

	@Override
	public UserInfo getUserInfo(String userName) {
		Long userId = userService.getUserIdByUserName(userName);
		return userId == null ? null : getUserInfo(userId);
	}

	@Override
	public UserInfo getUserInfoWithoutAvatar(long userId) {
		List<UserInfo> infos = jdbcTemplate.query(SELECT_INFO_WITHOUT_AVATAR_BY_USER_ID,
				USER_INFO_ROW_MAPPER, userId);
		return infos == null || infos.size() == 0 ? null : infos.get(0);
	}

	@Override
	public UserInfo getUserInfoWithoutAvatar(String userName) {
		Long userId = userService.getUserIdByUserName(userName);
		return userId == null ? null : getUserInfoWithoutAvatar(userId);
	}

	@Override
	public byte[] getUserAvatar(long userId) {
		List<byte[]> avatars = jdbcTemplate.query(SELECT_AVATAR_BY_USER_ID,
				new ByteArrayRowMapper(), userId);
		return avatars == null || avatars.size() == 0 ? null : avatars.get(0);
	}

	@Override
	public byte[] getUserAvatar(String userName) {
		Long userId = userService.getUserIdByUserName(userName);
		return userId == null ? null : getUserAvatar(userId);
	}
}
