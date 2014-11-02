package ru.anisimov.keybattle.data.service.interfaces;

import ru.anisimov.keybattle.model.user.UserInfo;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/31/14
 */
public interface UserInfoService {
	public boolean addInitialUserInfo(long userId);

	public boolean addInitialUserInfo(String userName);

	public boolean changeUserInfoWithoutAvatar(long userId, UserInfo userInfo);

	public boolean changeUserInfoWithoutAvatar(String userName, UserInfo userInfo);

	public boolean changeUserAvatar(long userId, byte[] avatar);

	public boolean changeUserAvatar(String userName, byte[] avatar);

	public boolean showUserInfo(long userId, boolean visible);

	public boolean showUserInfo(String userName, boolean visible);

	public UserInfo getUserInfo(long userId);

	public UserInfo getUserInfo(String userName);

	public UserInfo getUserInfoWithoutAvatar(long userId);

	public UserInfo getUserInfoWithoutAvatar(String userName);

	public byte[] getUserAvatar(long userId);

	public byte[] getUserAvatar(String userName);
}
