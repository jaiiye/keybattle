package ru.anisimov.keybattle.model.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/23/14
 */
public class UserFilter {
	public static final UserFilter NO_FILTER = new UserFilter();

	private List<String> users;

	public UserFilter() {
		this(new ArrayList<>());
	}

	public UserFilter(List<String> users) {
		this.users = users;
	}

	public UserFilter(String... users) {
		this.users = Arrays.asList(users);
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}
}
