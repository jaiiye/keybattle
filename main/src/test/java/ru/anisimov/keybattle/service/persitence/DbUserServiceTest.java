package ru.anisimov.keybattle.service.persitence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.anisimov.keybattle.config.CoreConfig;
import ru.anisimov.keybattle.config.PersistenceConfig;
import ru.anisimov.keybattle.model.user.KeybattleUser;
import ru.anisimov.keybattle.service.interfaces.UserService;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class, PersistenceConfig.class})
@TransactionConfiguration(defaultRollback = true)
public class DbUserServiceTest {
	@Autowired
	UserService userService;

	@Test
	public void testComplex() {
		userService.addUser(new KeybattleUser(0, "my_user", "mypass", true, Arrays.asList((GrantedAuthority) new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ADMIN")), "me@google.ru"));
		assertNotNull(userService.getByUserName("my_user"));
		assertTrue(userService.getByUserName("my_user").isEnabled());
		userService.deleteUser("my_user");
		assertFalse(userService.getByUserName("my_user").isEnabled());
	}
}
