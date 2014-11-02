package ru.anisimov.keybattle.model.user;

import org.springframework.security.core.GrantedAuthority;
import ru.anisimov.keybattle.security.AuthoritiesPolicy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/15/14
 */
public class UserBuilder {
	private String username;
	private String password;
	private String email;
	private boolean isLocked = false;
	private boolean isEnabled = true;
	private List<GrantedAuthority> authorities = new ArrayList<>();

	public UserBuilder(Registration registration) {
		this.username = registration.getUserName();
		this.password = registration.getPassword();
		this.email = registration.getEmail();
	}

	public UserBuilder(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public UserBuilder addRole(String role) {
		authorities.add(AuthoritiesPolicy.getAuthority(role));
		return this;
	}

	public User build() {
		return new User(0, username, password, isLocked, isEnabled, authorities, email);
	}
}
