package ru.anisimov.keybattle.service.persitence;

import ru.anisimov.keybattle.model.user.KeybattleUser;
import ru.anisimov.keybattle.service.interfaces.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/22/14
 */
public class MemoryUserService implements UserService {
	private List<KeybattleUser> users = Collections.synchronizedList(new ArrayList<>());

	@Override
	synchronized public boolean addUser(KeybattleUser user) {
		if (user != null && !users.stream().anyMatch(new UsernameEquals(user.getUsername()))) {
			users.add(user);
			return true;
		}
		return false;
	}

	@Override
	synchronized public boolean deleteUser(KeybattleUser user) {
		return user == null ? false : users.removeIf(new UsernameEquals(user.getUsername()));
	}

	@Override
	synchronized public boolean deleteUser(String userName) {
		return users.removeIf(new UsernameEquals(userName));
	}

	@Override
	public List<KeybattleUser> getUsers() {
		return Collections.unmodifiableList(users);
	}

	@Override
	synchronized public KeybattleUser getByUserName(String userName) {
		return users.stream()
				.filter(new UsernameEquals(userName))
				.findFirst().orElse(null);
	}

	@Override
	synchronized public KeybattleUser getByEmail(String email) {
		return users.stream()
				.filter(existingUser -> Objects.equals(existingUser.getEmail(), email))
				.findFirst().orElse(null);
	}

	private static class UsernameEquals implements Predicate<KeybattleUser> {
		private String userName;

		public UsernameEquals(String userName) {
			this.userName = userName;
		}

		@Override
		public boolean test(KeybattleUser user) {
			return Objects.equals(user.getUsername(), userName);
		}
	}
}
