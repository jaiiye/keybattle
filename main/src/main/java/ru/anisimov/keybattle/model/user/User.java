package ru.anisimov.keybattle.model.user;

import org.springframework.security.core.GrantedAuthority;
import ru.anisimov.keybattle.core.HasId;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/10/14
 */
public class User extends org.springframework.security.core.userdetails.User implements HasId {
	private long id;
	private String email;

	public User(long id, String username, String password, boolean locked, boolean enabled, Collection<? extends GrantedAuthority> authorities, String email) {
		super(username, password, enabled, true, true, !locked, authorities);
		this.id = id;
		this.email = email;
	}

	@Override
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

	private Object[] keyArray() {
		return new Object[]{getId(), getUsername(), getEmail(), getPassword(), getAuthorities().toArray(), isAccountNonLocked(), isEnabled()};
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(keyArray());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
	
		User that = (User) obj;
		return Arrays.deepEquals(this.keyArray(), that.keyArray());
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("User: ")
				.append(Arrays.toString(keyArray()))
				.toString();
	}
}
