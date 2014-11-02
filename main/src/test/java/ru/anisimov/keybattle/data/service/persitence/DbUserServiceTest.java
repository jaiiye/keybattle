package ru.anisimov.keybattle.data.service.persitence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.anisimov.keybattle.config.CoreConfig;
import ru.anisimov.keybattle.config.PersistenceConfig;
import ru.anisimov.keybattle.model.user.User;
import ru.anisimov.keybattle.model.user.UserBuilder;
import ru.anisimov.keybattle.data.service.interfaces.UserService;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class, PersistenceConfig.class})
@TransactionConfiguration(defaultRollback = true)
public class DbUserServiceTest {
	@Autowired
	UserService userService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Before
	public void setUp() throws Exception {
		jdbcTemplate.update("delete from user_role");
		jdbcTemplate.update("delete from user");
	}
	
	@Test
	public void testAddUser() throws Exception {		
		String userName = "vasya";
		String password = "vasya.vasyechkin";
		String email = "vasya.vasyechkin@mail.ru";
		
		assertNull(userService.getUserByUserName(userName));
		assertNull(userService.getUserByEmail(email));
		
		assertTrue(userService.addUser(new UserBuilder(userName, password, email).addRole("USER").addRole("ADMIN").build()));
		
		assertNotNull(userService.getUserByUserName(userName));
		assertNotNull(userService.getUserByEmail(email));
		
		User user = userService.getUserByUserName(userName);
		
		assertEquals(userName, user.getUsername());
		assertEquals(password, user.getPassword());
		assertEquals(email, user.getEmail());
		assertTrue(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "USER".equals(role)));
		assertTrue(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "ADMIN".equals(role)));
		assertTrue(user.isAccountNonLocked());
		assertTrue(user.isEnabled());
		assertTrue(user.isAccountNonExpired());
		assertTrue(user.isCredentialsNonExpired());

		jdbcTemplate.update("delete from user_role");
		user = userService.getUserByUserName(userName);
		assertEquals(userName, user.getUsername());
		assertEquals(password, user.getPassword());
		assertEquals(email, user.getEmail());
		assertTrue(user.isAccountNonLocked());
		assertTrue(user.isEnabled());
		assertTrue(user.isAccountNonExpired());
		assertTrue(user.isCredentialsNonExpired());
	}

	@Test(expected = DuplicateKeyException.class)
	public void testAddUserDuplicate() throws Exception {
		String userName = "vasya";
		String password = "vasya.vasyechkin";
		String email = "vasya.vasyechkin@mail.ru";

		assertNull(userService.getUserByUserName(userName));
		assertNull(userService.getUserByEmail(email));

		assertTrue(userService.addUser(new UserBuilder(userName, password, email).addRole("USER").addRole("ADMIN").build()));

		assertNotNull(userService.getUserByUserName(userName));
		assertNotNull(userService.getUserByEmail(email));

		User user = userService.getUserByUserName(userName);

		assertEquals(userName, user.getUsername());
		assertEquals(password, user.getPassword());
		assertEquals(email, user.getEmail());
		assertTrue(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "USER".equals(role)));
		assertTrue(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "ADMIN".equals(role)));
		assertTrue(user.isAccountNonLocked());
		assertTrue(user.isEnabled());
		assertTrue(user.isAccountNonExpired());
		assertTrue(user.isCredentialsNonExpired());

		assertFalse(userService.addUser(new UserBuilder(userName, "no_pass", email).addRole("USER").addRole("ADMIN").build()));
		assertFalse(userService.addUser(new UserBuilder("kolya", "no_pass", email).addRole("USER").addRole("ADMIN").build()));
		assertFalse(userService.addUser(new UserBuilder(userName, "no_pass", "kolya@mail.ru").addRole("USER").addRole("ADMIN").build()));
	}

	@Test
	public void testAddRole() throws Exception {
		String userName = "vasya";
		String password = "vasya.vasyechkin";
		String email = "vasya.vasyechkin@mail.ru";

		assertNull(userService.getUserByUserName(userName));
		assertNull(userService.getUserByEmail(email));

		assertTrue(userService.addUser(new UserBuilder(userName, password, email).addRole("USER").build()));

		assertNotNull(userService.getUserByUserName(userName));
		assertNotNull(userService.getUserByEmail(email));

		User user = userService.getUserByUserName(userName);

		assertEquals(userName, user.getUsername());
		assertEquals(password, user.getPassword());
		assertEquals(email, user.getEmail());
		assertTrue(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "USER".equals(role)));
		assertFalse(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "ADMIN".equals(role)));
		assertTrue(user.isAccountNonLocked());
		assertTrue(user.isEnabled());
		assertTrue(user.isAccountNonExpired());
		assertTrue(user.isCredentialsNonExpired());
		
		assertTrue(userService.addRole(userName, "ADMIN"));
		user = userService.getUserByUserName(userName);
		assertTrue(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "ADMIN".equals(role)));
	}

	@Test
	public void testRemoveRole() throws Exception {
		String userName = "vasya";
		String password = "vasya.vasyechkin";
		String email = "vasya.vasyechkin@mail.ru";

		assertNull(userService.getUserByUserName(userName));
		assertNull(userService.getUserByEmail(email));

		assertTrue(userService.addUser(new UserBuilder(userName, password, email).addRole("USER").addRole("ADMIN").build()));

		assertNotNull(userService.getUserByUserName(userName));
		assertNotNull(userService.getUserByEmail(email));

		User user = userService.getUserByUserName(userName);

		assertEquals(userName, user.getUsername());
		assertEquals(password, user.getPassword());
		assertEquals(email, user.getEmail());
		assertTrue(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "USER".equals(role)));
		assertTrue(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "ADMIN".equals(role)));
		assertTrue(user.isAccountNonLocked());
		assertTrue(user.isEnabled());
		assertTrue(user.isAccountNonExpired());
		assertTrue(user.isCredentialsNonExpired());

		assertTrue(userService.removeRole(userName, "ADMIN"));
		user = userService.getUserByUserName(userName);
		assertFalse(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "ADMIN".equals(role)));
		assertTrue(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "USER".equals(role)));

		assertTrue(userService.removeRole(userName, "USER"));
		user = userService.getUserByUserName(userName);
		assertFalse(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "ADMIN".equals(role)));
		assertFalse(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "USER".equals(role)));
	}

	@Test
	public void testLockUser() throws Exception {
		String userName = "vasya";
		String password = "vasya.vasyechkin";
		String email = "vasya.vasyechkin@mail.ru";

		assertNull(userService.getUserByUserName(userName));
		assertNull(userService.getUserByEmail(email));

		assertTrue(userService.addUser(new UserBuilder(userName, password, email).addRole("USER").build()));

		assertNotNull(userService.getUserByUserName(userName));
		assertNotNull(userService.getUserByEmail(email));

		User user = userService.getUserByUserName(userName);

		assertEquals(userName, user.getUsername());
		assertEquals(password, user.getPassword());
		assertEquals(email, user.getEmail());
		assertTrue(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "USER".equals(role)));
		assertFalse(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "ADMIN".equals(role)));
		assertTrue(user.isAccountNonLocked());
		assertTrue(user.isEnabled());
		assertTrue(user.isAccountNonExpired());
		assertTrue(user.isCredentialsNonExpired());

		assertTrue(userService.lockUser(userName, true));
		user = userService.getUserByUserName(userName);
		assertFalse(user.isAccountNonLocked());
		assertTrue(userService.lockUser(userName, false));
		user = userService.getUserByUserName(userName);
		assertTrue(user.isAccountNonLocked());
	}

	@Test
	public void testEnableUser() throws Exception {
		String userName = "vasya";
		String password = "vasya.vasyechkin";
		String email = "vasya.vasyechkin@mail.ru";

		assertNull(userService.getUserByUserName(userName));
		assertNull(userService.getUserByEmail(email));

		assertTrue(userService.addUser(new UserBuilder(userName, password, email).addRole("USER").build()));

		assertNotNull(userService.getUserByUserName(userName));
		assertNotNull(userService.getUserByEmail(email));

		User user = userService.getUserByUserName(userName);

		assertEquals(userName, user.getUsername());
		assertEquals(password, user.getPassword());
		assertEquals(email, user.getEmail());
		assertTrue(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "USER".equals(role)));
		assertFalse(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "ADMIN".equals(role)));
		assertTrue(user.isAccountNonLocked());
		assertTrue(user.isEnabled());
		assertTrue(user.isAccountNonExpired());
		assertTrue(user.isCredentialsNonExpired());

		assertTrue(userService.enableUser(userName, false));
		user = userService.getUserByUserName(userName);
		assertFalse(user.isEnabled());
		assertTrue(userService.enableUser(userName, true));
		user = userService.getUserByUserName(userName);
		assertTrue(user.isEnabled());
	}

	@Test
	public void testGetUsers() throws Exception {
		String userName = "vasya";
		String password = "vasya.vasyechkin";
		String email = "vasya.vasyechkin@mail.ru";

		assertNull(userService.getUserByUserName(userName));
		assertNull(userService.getUserByEmail(email));

		assertTrue(userService.addUser(new UserBuilder(userName, password, email).addRole("USER").build()));

		assertNotNull(userService.getUserByUserName(userName));
		assertNotNull(userService.getUserByEmail(email));

		String userName2 = "vasya2";
		String password2 = "vasya2.vasyechkin";
		String email2 = "vasya2.vasyechkin@mail.ru";

		assertNull(userService.getUserByUserName(userName2));
		assertNull(userService.getUserByEmail(email2));

		assertTrue(userService.addUser(new UserBuilder(userName2, password2, email2).addRole("USER").build()));

		assertNotNull(userService.getUserByUserName(userName2));
		assertNotNull(userService.getUserByEmail(email2));

		User user = userService.getUserByUserName(userName);

		assertEquals(userName, user.getUsername());
		assertEquals(password, user.getPassword());
		assertEquals(email, user.getEmail());
		assertTrue(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "USER".equals(role)));
		assertFalse(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "ADMIN".equals(role)));
		assertTrue(user.isAccountNonLocked());
		assertTrue(user.isEnabled());
		assertTrue(user.isAccountNonExpired());
		assertTrue(user.isCredentialsNonExpired());

		user = userService.getUserByUserName(userName2);

		assertEquals(userName2, user.getUsername());
		assertEquals(password2, user.getPassword());
		assertEquals(email2, user.getEmail());
		assertTrue(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "USER".equals(role)));
		assertFalse(user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role -> "ADMIN".equals(role)));
		assertTrue(user.isAccountNonLocked());
		assertTrue(user.isEnabled());
		assertTrue(user.isAccountNonExpired());
		assertTrue(user.isCredentialsNonExpired());
	}

	@Test
	public void testGetUsersByRole() throws Exception {
		String userName = "vasya";
		String password = "vasya.vasyechkin";
		String email = "vasya.vasyechkin@mail.ru";

		assertNull(userService.getUserByUserName(userName));
		assertNull(userService.getUserByEmail(email));

		assertTrue(userService.addUser(new UserBuilder(userName, password, email).addRole("USER").build()));

		assertNotNull(userService.getUserByUserName(userName));
		assertNotNull(userService.getUserByEmail(email));

		String userName2 = "vasya2";
		String password2 = "vasya2.vasyechkin";
		String email2 = "vasya2.vasyechkin@mail.ru";

		assertNull(userService.getUserByUserName(userName2));
		assertNull(userService.getUserByEmail(email2));

		assertTrue(userService.addUser(new UserBuilder(userName2, password2, email2).addRole("USER").addRole("ADMIN").build()));

		assertNotNull(userService.getUserByUserName(userName2));
		assertNotNull(userService.getUserByEmail(email2));


		String userName3 = "vasya3";
		String password3 = "vasya3.vasyechkin";
		String email3 = "vasya3.vasyechkin@mail.ru";

		assertNull(userService.getUserByUserName(userName3));
		assertNull(userService.getUserByEmail(email3));

		assertTrue(userService.addUser(new UserBuilder(userName3, password3, email3).addRole("USER").addRole("ADMIN").build()));

		assertNotNull(userService.getUserByUserName(userName3));
		assertNotNull(userService.getUserByEmail(email3));
		
		assertEquals(3, userService.getUsers("USER").size());
		assertTrue(userService.getUsers("USER").stream().allMatch(
				user -> user.getUsername().equals("vasya")
						|| user.getUsername().equals("vasya2")
						|| user.getUsername().equals("vasya3"))
		);
		assertTrue(userService.getUsers("USER").stream().anyMatch(
						user -> user.getUsername().equals("vasya"))
		);
		assertTrue(userService.getUsers("USER").stream().anyMatch(
						user -> user.getUsername().equals("vasya2"))
		);
		assertTrue(userService.getUsers("USER").stream().anyMatch(
						user -> user.getUsername().equals("vasya3"))
		);

		assertEquals(2, userService.getUsers("ADMIN").size());
		assertTrue(userService.getUsers("ADMIN").stream().allMatch(
						user -> user.getUsername().equals("vasya2")
								|| user.getUsername().equals("vasya3"))
		);
		assertTrue(userService.getUsers("ADMIN").stream().anyMatch(
						user -> user.getUsername().equals("vasya3"))
		);
		assertTrue(userService.getUsers("ADMIN").stream().anyMatch(
						user -> user.getUsername().equals("vasya2"))
		);
		
		userService.removeRole("vasya3", "ADMIN");
		assertEquals(1, userService.getUsers("ADMIN").size());
		assertTrue(userService.getUsers("ADMIN").stream().anyMatch(
						user -> user.getUsername().equals("vasya2"))
		);
	}

	@Test
	public void testGetUserById() throws Exception {
		String userName = "vasya";
		String password = "vasya.vasyechkin";
		String email = "vasya.vasyechkin@mail.ru";

		assertNull(userService.getUserByUserName(userName));
		assertNull(userService.getUserByEmail(email));

		assertTrue(userService.addUser(new UserBuilder(userName, password, email).addRole("USER").addRole("ADMIN").build()));

		assertNotNull(userService.getUserByUserName(userName));
		assertNotNull(userService.getUserByEmail(email));

		User user = userService.getUserByUserName(userName);
		
		assertEquals(user, userService.getUserById(user.getId()));
	}
	
	@Test
	public void testGetUserIdByUserName() throws Exception {
		String userName = "vasya";
		String password = "vasya.vasyechkin";
		String email = "vasya.vasyechkin@mail.ru";

		assertNull(userService.getUserByUserName(userName));
		assertNull(userService.getUserByEmail(email));

		assertTrue(userService.addUser(new UserBuilder(userName, password, email).addRole("USER").addRole("ADMIN").build()));

		assertNotNull(userService.getUserByUserName(userName));
		assertNotNull(userService.getUserByEmail(email));

		User user = userService.getUserByUserName(userName);

		assertEquals(user.getId(), (long)userService.getUserIdByUserName(userName));
	}

	@Test
	public void testGetUserByEmail() throws Exception {
		String userName = "vasya";
		String password = "vasya.vasyechkin";
		String email = "vasya.vasyechkin@mail.ru";

		assertNull(userService.getUserByUserName(userName));
		assertNull(userService.getUserByEmail(email));

		assertTrue(userService.addUser(new UserBuilder(userName, password, email).addRole("USER").addRole("ADMIN").build()));

		assertNotNull(userService.getUserByUserName(userName));
		assertNotNull(userService.getUserByEmail(email));

		User user = userService.getUserByUserName(userName);

		assertEquals(user, userService.getUserByEmail(email));
	}
}
