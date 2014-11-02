package ru.anisimov.keybattle.data.service.persitence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.anisimov.keybattle.config.CoreConfig;
import ru.anisimov.keybattle.config.PersistenceConfig;
import ru.anisimov.keybattle.model.user.Gender;
import ru.anisimov.keybattle.model.user.User;
import ru.anisimov.keybattle.model.user.UserInfo;
import ru.anisimov.keybattle.security.AuthoritiesPolicy;
import ru.anisimov.keybattle.data.service.interfaces.UserInfoService;
import ru.anisimov.keybattle.data.service.interfaces.UserService;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class, PersistenceConfig.class})
@TransactionConfiguration(defaultRollback = true)
public class DbUserInfoServiceTest {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private UserService userService;
	@Autowired
	private UserInfoService userInfoService;
	
	@Before
	public void setUp() throws Exception {
		jdbcTemplate.update("delete from user_info");
	}

	@Test
	public void testAddInitialUserInfo() throws Exception {
		assertTrue(userService.addUser(new User(0, "valter", "no_pass", false, true, 
				Arrays.asList(AuthoritiesPolicy.getAuthority("USER")), "valter@mail.ru")));
		assertTrue(userInfoService.addInitialUserInfo("valter"));
		User user = userService.getUserByUserName("valter");
		UserInfo userInfo = userInfoService.getUserInfo("valter");
		assertEquals(user.getId(), userInfo.getUserId());
		assertNull(userInfo.getAge());
		assertNull(userInfo.getAvatar());
		assertNull(userInfo.getCountry());
		assertNull(userInfo.getDateOfBirth());
		assertEquals(Gender.ALIEN, userInfo.getGender());
		assertNull(userInfo.getName());
		assertNull(userInfo.getStatus());
		assertEquals(true, userInfo.isHidden());
	}

	@Test
	public void testShowUserInfo() throws Exception {
		assertTrue(userService.addUser(new User(0, "valter", "no_pass", false, true,
				Arrays.asList(AuthoritiesPolicy.getAuthority("USER")), "valter@mail.ru")));
		assertTrue(userInfoService.addInitialUserInfo("valter"));
		User user = userService.getUserByUserName("valter");
		UserInfo userInfo = userInfoService.getUserInfo("valter");
		assertEquals(user.getId(), userInfo.getUserId());
		assertNull(userInfo.getAge());
		assertNull(userInfo.getAvatar());
		assertNull(userInfo.getCountry());
		assertNull(userInfo.getDateOfBirth());
		assertEquals(Gender.ALIEN, userInfo.getGender());
		assertNull(userInfo.getName());
		assertNull(userInfo.getStatus());
		assertEquals(true, userInfo.isHidden());
		
		assertTrue(userInfoService.showUserInfo("valter", true));
		userInfo = userInfoService.getUserInfo("valter");
		assertFalse(userInfo.isHidden());
	}

	@Test
	public void testGetUserInfo() throws Exception {
		assertTrue(userService.addUser(new User(0, "valter", "no_pass", false, true,
				Arrays.asList(AuthoritiesPolicy.getAuthority("USER")), "valter@mail.ru")));
		assertTrue(userInfoService.addInitialUserInfo("valter"));
		User user = userService.getUserByUserName("valter");
		
		byte[] avatar = new byte[]{0, 0 , 1, 0, 1, 0, 2, 5, 1, 4};
		UserInfo expectedInfo = new UserInfo();
		expectedInfo.setAge(3);
		expectedInfo.setAvatar(avatar);
		expectedInfo.setCountry("Russian Federation");
		expectedInfo.setDateOfBirth(LocalDate.of(2010, 12, 24));
		expectedInfo.setGender(Gender.FEMALE);
		expectedInfo.setHidden(false);
		expectedInfo.setName("Vasya");
		expectedInfo.setStatus("come and c me");
		expectedInfo.setUserId(user.getId());
		
		assertTrue(userInfoService.changeUserInfoWithoutAvatar("valter", expectedInfo));
		UserInfo userInfo = userInfoService.getUserInfo("valter");
		expectedInfo.setAvatar(null);
		assertEquals(expectedInfo, userInfo);
		assertTrue(userInfoService.changeUserAvatar("valter", avatar));
		userInfo = userInfoService.getUserInfo("valter");
		expectedInfo.setAvatar(avatar);
		assertEquals(expectedInfo, userInfo);
	}

	@Test
	public void testGetUserInfoWithoutAvatar() throws Exception {
		assertTrue(userService.addUser(new User(0, "valter", "no_pass", false, true,
				Arrays.asList(AuthoritiesPolicy.getAuthority("USER")), "valter@mail.ru")));
		assertTrue(userInfoService.addInitialUserInfo("valter"));
		User user = userService.getUserByUserName("valter");

		byte[] avatar = new byte[]{0, 0 , 1, 0, 1, 0, 2, 5, 1, 4};
		UserInfo expectedInfo = new UserInfo();
		expectedInfo.setAge(3);
		expectedInfo.setAvatar(avatar);
		expectedInfo.setCountry("Russian Federation");
		expectedInfo.setDateOfBirth(LocalDate.of(2010, 12, 24));
		expectedInfo.setGender(Gender.FEMALE);
		expectedInfo.setHidden(false);
		expectedInfo.setName("Vasya");
		expectedInfo.setStatus("come and c me");
		expectedInfo.setUserId(user.getId());

		assertTrue(userInfoService.changeUserInfoWithoutAvatar("valter", expectedInfo));
		UserInfo userInfo = userInfoService.getUserInfoWithoutAvatar("valter");
		expectedInfo.setAvatar(null);
		assertEquals(expectedInfo, userInfo);
		assertTrue(userInfoService.changeUserAvatar("valter", avatar));
		userInfo = userInfoService.getUserInfoWithoutAvatar("valter");
		assertEquals(expectedInfo, userInfo);
	}

	@Test
	public void testGetUserAvatar() throws Exception {
		assertTrue(userService.addUser(new User(0, "valter", "no_pass", false, true,
				Arrays.asList(AuthoritiesPolicy.getAuthority("USER")), "valter@mail.ru")));
		assertTrue(userInfoService.addInitialUserInfo("valter"));
		User user = userService.getUserByUserName("valter");

		byte[] avatar = new byte[]{0, 0 , 1, 0, 1, 0, 2, 5, 1, 4};
		UserInfo expectedInfo = new UserInfo();
		expectedInfo.setAge(3);
		expectedInfo.setAvatar(avatar);
		expectedInfo.setCountry("Russian Federation");
		expectedInfo.setDateOfBirth(LocalDate.of(2010, 12, 24));
		expectedInfo.setGender(Gender.FEMALE);
		expectedInfo.setHidden(false);
		expectedInfo.setName("Vasya");
		expectedInfo.setStatus("come and c me");
		expectedInfo.setUserId(user.getId());

		assertTrue(userInfoService.changeUserInfoWithoutAvatar("valter", expectedInfo));
		assertTrue(userInfoService.changeUserAvatar("valter", avatar));
		assertArrayEquals(avatar, userInfoService.getUserAvatar("valter"));
	}
}
