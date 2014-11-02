package ru.anisimov.keybattle.data.service.persitence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.anisimov.keybattle.config.CoreConfig;
import ru.anisimov.keybattle.config.PersistenceConfig;
import ru.anisimov.keybattle.model.history.HistoryAction;
import ru.anisimov.keybattle.model.history.UserHistoryRecord;
import ru.anisimov.keybattle.model.user.FakeUsers;
import ru.anisimov.keybattle.model.user.User;
import ru.anisimov.keybattle.data.service.interfaces.UserHistoryService;
import ru.anisimov.keybattle.data.service.interfaces.UserService;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class, PersistenceConfig.class})
@TransactionConfiguration(defaultRollback = true)
public class DbUserHistoryServiceTest {
	@Autowired
	UserHistoryService userHistoryService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	UserService userService;
	
	@Before
	public void setUp() throws Exception {
		jdbcTemplate.update("delete from user_history");
	}
	
	@Test
	public void testAddHistoryRecord() throws Exception {
		assertTrue(userService.addUser(new User(0, "valter", "no_pass", false, true, 
				Arrays.asList(new SimpleGrantedAuthority("USER")), "valter@mail.ru")));
		assertTrue(userHistoryService.addHistoryRecord("valter", HistoryAction.USER_ADDED, FakeUsers.SYSTEM));
		List<UserHistoryRecord> records = userHistoryService.getRecordsByUser("valter");
		User user = userService.getUserByUserName("valter");
		assertEquals(1, records.size());
		assertEquals(user.getId(), records.get(0).getUserId());
		assertEquals(HistoryAction.USER_ADDED, records.get(0).getActionId());
		assertEquals(FakeUsers.SYSTEM, records.get(0).getActorId());
		assertEquals("USER_ADDED", records.get(0).getActionName());

		List<UserHistoryRecord> records2 = userHistoryService.getRecordsByUser("valter2");
		assertEquals(0, records2.size());
		
		List<UserHistoryRecord> recordsByActor = userHistoryService.getRecordsByActor(FakeUsers.SYSTEM);
		List<UserHistoryRecord> recordsByActor2 = userHistoryService.getRecordsByActor(FakeUsers.SYSTEM - 1);
		assertEquals(0, recordsByActor2.size());
		assertArrayEquals(records.toArray(), recordsByActor.toArray());
		
		List<UserHistoryRecord> recordsByUserAndAction = userHistoryService.getRecordsByUserAndAction("valter", HistoryAction.USER_ADDED);
		assertEquals(1, recordsByUserAndAction.size());
		List<UserHistoryRecord> recordsByUserAndAction2 = userHistoryService.getRecordsByUserAndAction("valter2", HistoryAction.USER_ADDED);
		assertEquals(0, recordsByUserAndAction2.size());
		List<UserHistoryRecord> recordsByUserAndAction3 = userHistoryService.getRecordsByUserAndAction("valter", HistoryAction.USER_ADDED + 1);
		assertEquals(0, recordsByUserAndAction3.size());

		assertArrayEquals(records.toArray(), recordsByUserAndAction.toArray());
	}
}
