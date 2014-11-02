package ru.anisimov.keybattle.data.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import ru.anisimov.keybattle.core.HasTransactions;
import ru.anisimov.keybattle.model.history.HistoryAction;
import ru.anisimov.keybattle.model.history.UserHistoryRecord;
import ru.anisimov.keybattle.model.user.User;
import ru.anisimov.keybattle.model.user.UserInfo;
import ru.anisimov.keybattle.data.service.interfaces.UserHistoryService;
import ru.anisimov.keybattle.data.service.interfaces.UserInfoService;
import ru.anisimov.keybattle.data.service.interfaces.UserService;

import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/31/14
 */
@Component
public class UserManager implements HasTransactions {
	@Autowired
	private TransactionTemplate transactionTemplate;
	@Autowired
	private UserService userService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private UserHistoryService userHistoryService;

	public boolean addUser(User user, long actorId) {
		return doInTransactionWithoutException(
				() -> userService.addUser(user)
						&& userHistoryService.addHistoryRecord(user.getUsername(), HistoryAction.USER_ADDED, actorId)
						&& userInfoService.addInitialUserInfo(user.getUsername())
						&& userHistoryService.addHistoryRecord(user.getUsername(), HistoryAction.USER_INFO_ADDED, actorId),
				String.format("Could not add user %s by actor %d", user, actorId)
		);
	}

	public boolean addUserRole(String userName, String role, long actorId) {
		return doInTransactionWithoutException(
				() -> userService.addRole(userName, role)
						&& userHistoryService.addHistoryRecord(userName, HistoryAction.USER_ROLE_ADDED, actorId),
				String.format("Could not add for user %s role %s by actor %d", userName, role, actorId)
		);
	}

	public boolean removeUserRole(String userName, String role, long actorId) {
		return doInTransactionWithoutException(
				() -> userService.removeRole(userName, role)
						&& userHistoryService.addHistoryRecord(userName, HistoryAction.USER_ROLE_REMOVED, actorId),
				String.format("Could not remove for user %s role %s by actor %d", userName, role, actorId)
		);
	}

	public boolean lockUser(String userName, long actorId) {
		return doInTransactionWithoutException(
				() -> userService.lockUser(userName, true)
						&& userHistoryService.addHistoryRecord(userName, HistoryAction.USER_LOCKED, actorId),
				String.format("Could not lock user % by actor %d", userName, actorId)
		);
	}

	public boolean unlockUser(String userName, long actorId) {
		return doInTransactionWithoutException(
				() -> userService.lockUser(userName, false)
						&& userHistoryService.addHistoryRecord(userName, HistoryAction.USER_UNLOCKED, actorId),
				String.format("Could not lock user % by actor %d", userName, actorId)
		);
	}

	public boolean disableUser(String userName, long actorId) {
		return doInTransactionWithoutException(
				() -> userService.enableUser(userName, false)
						&& userHistoryService.addHistoryRecord(userName, HistoryAction.USER_DISABLED, actorId),
				String.format("Could not disable user %s by actor %d", userName, actorId)
		);
	}

	public boolean enableUser(String userName, long actorId) {
		return doInTransactionWithoutException(
				() -> userService.enableUser(userName, true)
						&& userHistoryService.addHistoryRecord(userName, HistoryAction.USER_ENABLED, actorId),
				String.format("Could not disable user %s by actor %d", userName, actorId)
		);
	}

	public List<User> getUsers() {
		return getWithoutException(
				() -> userService.getUsers(),
				"Could not get user list"
		);
	}

	public List<User> getUsers(String role) {
		return getWithoutException(
				() -> userService.getUsers(role),
				String.format("Could not get user list for role %s", role)  
		);
	}

	public User getUserById(long userId) {
		return getWithoutException(
				() -> userService.getUserById(userId),
				String.format("Could not get user with id %d", userId)
		);
	}

	public User getUserByUserName(String userName) {
		return getWithoutException(
				() -> userService.getUserByUserName(userName),
				String.format("Could not get user with name %s", userName)  
		);
	}

	public User getUserByEmail(String email) {
		return getWithoutException(
				() -> userService.getUserByEmail(email),
				String.format("Could not get user with email %s", email)
		);
	}

	public boolean changeUserInfo(String userName, UserInfo userInfo, long actorId) {
		return doInTransactionWithoutException(
				() -> userInfoService.changeUserInfoWithoutAvatar(userName, userInfo) 
						&& userHistoryService.addHistoryRecord(userName, HistoryAction.USER_INFO_CHANGED, actorId),
				String.format("Could not change user info to %s for user %s by actor %d", String.valueOf(userInfo), userName, actorId)
		);
	}

	public boolean showUserInfo(String userName, long actorId) {
		return doInTransactionWithoutException(
				() -> userInfoService.showUserInfo(userName, true)
						&& userHistoryService.addHistoryRecord(userName, HistoryAction.USER_INFO_SHOWN, actorId),
				String.format("Could not show user info for user %s by actor %d", userName, actorId)
		);
	}

	public boolean hideUserInfo(String userName, long actorId) {
		return doInTransactionWithoutException(
				() -> userInfoService.showUserInfo(userName, false)
						&& userHistoryService.addHistoryRecord(userName, HistoryAction.USER_INFO_HIDDEN, actorId),
				String.format("Could not hide user info for user %s by actor %d", userName, actorId)
		);
	}

	public UserInfo getUserInfo(String userName) {
		return getWithoutException(
				() -> userInfoService.getUserInfo(userName),
				String.format("Could not get user info for user %s", userName)
		);
	}

	public UserInfo getUserInfoWithoutAvatar(String userName) {
		return getWithoutException(
				() -> userInfoService.getUserInfoWithoutAvatar(userName),
				String.format("Could not get user info without avatar for user %s", userName)
		);
	}
	
	public byte[] getUserAvatar(String userName) {
		return getWithoutException(
				() -> userInfoService.getUserAvatar(userName),
				String.format("Could not get user avatar for user %s", userName)
		);
	}

	public List<UserHistoryRecord> getRecordsByUserAndAction(String userName, int actionId) {
		return getWithoutException(
				() -> userHistoryService.getRecordsByUserAndAction(userName, actionId),
				String.format("Could not get user history for user %s action %d", userName, actionId)
		);
	}

	public List<UserHistoryRecord> getRecordsByUser(String userName) {
		return getWithoutException(
				() -> userHistoryService.getRecordsByUser(userName),
				String.format("Could not get user history for user %s", userName)
		);
	}

	public List<UserHistoryRecord> getRecordsByActor(long actorId) {
		return getWithoutException(
				() -> userHistoryService.getRecordsByActor(actorId),
				String.format("Could not get user history for user %d", actorId)
		);
	}
	
	@Override
	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}
}
