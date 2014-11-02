package ru.anisimov.keybattle.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/31/14
 */
public class AuthoritiesPolicy {
	public static final GrantedAuthority getAuthority(String role) {
		return new SimpleGrantedAuthority(role);
	}
}
