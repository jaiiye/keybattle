package ru.anisimov.keybattle.data.service.persitence;

import ru.anisimov.keybattle.model.user.User;
import ru.anisimov.keybattle.data.service.interfaces.impl.AbstractUserService;

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
public class MemoryUserService extends AbstractUserService {
	private List<User> users = Collections.synchronizedList(new ArrayList<>());

	@Override
	synchronized public boolean addUser(User user) {
		if (user != null && !users.stream().anyMatch(new UsernameEquals(user.getUsername()))) {
			users.add(user);
			return true;
		}
		return false;
	}

	@Override
	synchronized public boolean removeUser(String userName) {
		return users.removeIf(new UsernameEquals(userName));
	}

	@Override
	public List<User> getUsers() {
		return Collections.unmodifiableList(users);
	}

	@Override
	synchronized public User getUserByUserName(String userName) {
		return users.stream()
				.filter(new UsernameEquals(userName))
				.findFirst().orElse(null);
	}

	@Override
	synchronized public User getUserByEmail(String email) {
		return users.stream()
				.filter(existingUser -> Objects.equals(existingUser.getEmail(), email))
				.findFirst().orElse(null);
	}

	private static class UsernameEquals implements Predicate<User> {
		private String userName;

		public UsernameEquals(String userName) {
			this.userName = userName;
		}

		@Override
		public boolean test(User user) {
			return Objects.equals(user.getUsername(), userName);
		}
	}
}
