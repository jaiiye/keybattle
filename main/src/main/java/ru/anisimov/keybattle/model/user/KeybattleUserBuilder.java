package ru.anisimov.keybattle.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/15/14
 */
public class KeybattleUserBuilder {
	private String username;
	private String password;
	private String email;
	private boolean isEnabled = true;
	private List<GrantedAuthority> authorities = new ArrayList<>();

	public KeybattleUserBuilder(Registration registration) {
		this.username = registration.getUserName();
		this.password = registration.getPassword();
		this.email = registration.getEmail();
	}

	public KeybattleUserBuilder(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public KeybattleUserBuilder addRole(String role) {
		authorities.add(new SimpleGrantedAuthority(role));
		return this;
	}

	public KeybattleUser build() {
		return new KeybattleUser(0, username, password, isEnabled, authorities, email);
	}
}
