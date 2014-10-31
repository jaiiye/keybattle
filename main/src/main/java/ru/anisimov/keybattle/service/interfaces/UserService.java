package ru.anisimov.keybattle.service.interfaces;

import ru.anisimov.keybattle.model.user.KeybattleUser;

import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/14/14
 */
public interface UserService {
	boolean addUser(KeybattleUser user);

	boolean deleteUser(KeybattleUser user);

	boolean deleteUser(String userName);

	public List<KeybattleUser> getUsers();

	KeybattleUser getByUserName(String userName);

	KeybattleUser getByEmail(String email);
}
