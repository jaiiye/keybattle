package ru.anisimov.keybattle.data.service;

import ru.anisimov.keybattle.model.user.User;
import ru.anisimov.keybattle.data.service.interfaces.UserService;

import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/31/14
 */
public class AbstractUserService implements UserService {
	@Override
	public boolean addUser(User user) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeUser(long userId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeUser(String userName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addRole(long userId, String role) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addRole(String userName, String role) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeRole(long userId, String role) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeRole(String userName, String role) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean lockUser(long userId, boolean locked) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean lockUser(String userName, boolean locked) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean enableUser(long userId, boolean enabled) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean enableUser(String userName, boolean enabled) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<User> getUsers() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<User> getUsers(String role) {
		throw new UnsupportedOperationException();
	}

	@Override
	public User getUserById(long userId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Long getUserIdByUserName(String userName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public User getUserByUserName(String userName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public User getUserByEmail(String email) {
		throw new UnsupportedOperationException();
	}
}
