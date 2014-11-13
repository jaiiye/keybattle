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
		result.setUserId(rs.getLong("USER_ID"));
		result.setName(rs.getString("NAME"));
		int age = rs.getInt("AGE");
		result.setAge(age == 0 ? null : age);
		result.setGender(Gender.values()[rs.getInt("GENDER")]);
		Date date = rs.getDate("DOB");
		result.setDateOfBirth(date == null ? null : date.toLocalDate());
		result.setCountry(rs.getString("COUNTRY"));
		result.setStatus(rs.getString("STATUS"));
		result.setAvatar(rs.getBytes("AVATAR"));
		result.setHidden(rs.getInt("HIDDEN") == 1);
		return result;
	};

	private static final String SELECT_INFO_BY_USER_ID = "SELECT * FROM V_USER_INFO WHERE USER_ID = ?";

	private static final String SELECT_INFO_WITHOUT_AVATAR_BY_USER_ID = "SELECT USER_ID, NAME, AGE, GENDER, DOB, " +
			" COUNTRY, STATUS, NULL AVATAR, HIDDEN FROM V_USER_INFO WHERE USER_ID = ?";

	private static final String SELECT_AVATAR_BY_USER_ID = "SELECT AVATAR FROM V_USER_INFO WHERE USER_ID = ?";

	private static final String SHOW_USER_INFO_BY_USER_ID = "UPDATE USER_INFO SET MODIFICATION_TIME = NOW(), " +
			" HIDDEN = ? WHERE USER_ID = ?";

	private static final String CHANGE_USER_AVATAR_BY_USER_ID = "UPDATE USER_INFO SET MODIFICATION_TIME = NOW(), " +
			" AVATAR = ? WHERE USER_ID = ?";

	private static final String CHANGE_USER_INFO_BY_USER_ID = "UPDATE USER_INFO SET MODIFICATION_TIME = NOW(), " +
			" NAME = ?, GENDER = ?, DOB = ?, COUNTRY = ?, STATUS = ?, HIDDEN = ? WHERE USER_ID = ?";

	private static final String ADD_INITIAL_INFO = "INSERT INTO USER_INFO (USER_ID) VALUES (?)";

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
