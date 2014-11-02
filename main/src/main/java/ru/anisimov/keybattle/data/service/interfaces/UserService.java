package ru.anisimov.keybattle.data.service.interfaces;

import ru.anisimov.keybattle.model.user.User;

import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/14/14
 */
public interface UserService {
	boolean addUser(User user);

	boolean removeUser(long userId);
	
	boolean removeUser(String userName);

	boolean addRole(long userId, String role);

	boolean addRole(String userName, String role);

	boolean removeRole(long userId, String role);

	boolean removeRole(String userName, String role);

	boolean lockUser(long userId, boolean locked);

	boolean lockUser(String userName, boolean locked);

	boolean enableUser(long userId, boolean enabled);

	boolean enableUser(String userName, boolean enabled);

	public List<User> getUsers();

	public List<User> getUsers(String role);

	User getUserById(long userId);

	Long getUserIdByUserName(String userName);

	User getUserByUserName(String userName);

	User getUserByEmail(String email);
}
