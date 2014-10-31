package ru.anisimov.keybattle.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/10/14
 */
public class KeybattleUser extends User {
	private long id;
	private String email;

	public KeybattleUser(long id, String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities, String email) {
		super(username, password, enabled, true, true, true, authorities);
		this.id = id;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(super.toString()).append("; ")
				.append("Id: ").append(getId()).append("; ")
				.append("Email: ").append(getEmail()).append("; ");
		return result.toString();
	}
}
