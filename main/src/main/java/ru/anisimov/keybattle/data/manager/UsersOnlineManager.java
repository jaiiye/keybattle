package ru.anisimov.keybattle.data.manager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.anisimov.keybattle.websocket.action.SendWebsocketMessage;
import ru.anisimov.keybattle.core.ObjectBuilder;
import ru.anisimov.keybattle.core.ObjectBuilderWithParam;
import ru.anisimov.keybattle.core.Pair;
import ru.anisimov.keybattle.model.chat.*;
import ru.anisimov.keybattle.model.user.User;
import ru.anisimov.keybattle.data.service.interfaces.UserService;
import ru.anisimov.keybattle.data.service.persitence.MemoryUserService;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static ru.anisimov.keybattle.config.DestinationConfig.BATTLE_ROOM_CHAT_ADDRESS;
import static ru.anisimov.keybattle.config.DestinationConfig.BATTLE_ROOM_USERS_ADDRESS;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/22/14
 */
public class UsersOnlineManager {
	private static final Logger log = Logger.getLogger(UsersOnlineManager.class);

	@Autowired
	private UserManager userManager;
	@Autowired
	private ChatLogManager chatLogManager;

	private SimpMessagingTemplate template;

	private Map<String, PersistentConnection> sessionPlaces = new HashMap<>();
	private Map<PersistentConnection, Place> places = new HashMap<>();

	{
		places.put(PersistentConnection.BATTLE_ROOM, new Place());
	}

	@Autowired
	public UsersOnlineManager(SimpMessagingTemplate template) {
		this.template = template;
	}

	synchronized public boolean addUserSession(String userName, String sessionId, PersistentConnection placeType) {
		if (sessionPlaces.containsKey(sessionId)) {
			return false;
		}

		log.debug("Manager adding user " + userName);

		Place place = places.get(placeType);
		Map<String, Set<String>> userSessions = place.getUserSessions();
		UserService placeUsers = place.getUserService();

		User user = userManager.getUserByUserName(userName);
		if (!userSessions.containsKey(userName)) {
			userSessions.put(userName, new HashSet<>());
		}
		Set<String> sessions = userSessions.get(userName);
		if (sessions.size() == 0) {
			placeUsers.addUser(user);
			placeType.notifyListeners(ActionType.ADD, userName);
			log.debug("Manager added user " + userName);
		}
		sessionPlaces.put(sessionId, placeType);
		sessions.add(sessionId);
		return true;
	}

	synchronized public boolean removeUserSession(String userName, String sessionId) {
		if (!sessionPlaces.containsKey(sessionId)) {
			return false;
		}
		PersistentConnection placeType = sessionPlaces.get(sessionId);
		Place place = places.get(placeType);
		Map<String, Set<String>> userSessions = place.getUserSessions();
		UserService placeUsers = place.getUserService();

		if (!userSessions.containsKey(userName)) {
			return false;
		}
		Set<String> sessions = userSessions.get(userName);
		if (sessions.remove(sessionId)) {
			sessionPlaces.remove(sessionId);
			if (sessions.size() == 0) {
				placeUsers.removeUser(userName);
				placeType.notifyListeners(ActionType.REMOVE, userName);
				return true;
			}
		}
		return false;
	}

	public List<String> getUsers(PersistentConnection placeType) {
		return places.get(placeType).getUserService().getUsers().stream()
				.map(User::getUsername)
				.collect(Collectors.toList());
	}

	@PostConstruct
	public void init() {
		PersistentConnection.setSimpMessagingTemplate(template);
		PersistentConnection.setChatLogManager(chatLogManager);
	}

	public enum PersistentConnection {
		BATTLE_ROOM(new HashMap<ActionType, Pair<String, ObjectBuilderWithParam>[]>() {{
			put(ActionType.ADD, new Pair[] {
					new Pair<>(BATTLE_ROOM_CHAT_ADDRESS, new ObjectBuilderWithParam() {
						@Override
						public Object build() {
							String userName = (String) getParam();
							String messageString = "User " + userName + " joined the conversation";
							ChatEvent event = new ChatEvent(new Date(), TimeEventType.USER_JOIN,
									Arrays.asList(new ChatMessage(userName, messageString, UserFilter.NO_FILTER)));
							return event;
						}
					}),
					new Pair<>(BATTLE_ROOM_USERS_ADDRESS, new ObjectBuilderWithParam() {
						@Override
						public Object build() {
							String userName = (String) getParam();
							return new UserEvent(new Date(), TimeEventType.USER_JOIN, Arrays.asList(userName));
						}
					})
			});

			put(ActionType.REMOVE, new Pair[] {
					new Pair<>(BATTLE_ROOM_CHAT_ADDRESS, new ObjectBuilderWithParam() {
						@Override
						public Object build() {
							String userName = (String) getParam();
							String messageString = "User " + userName + " has left the conversation";
							ChatEvent event = new ChatEvent(new Date(), TimeEventType.USER_LEAVE,
									Arrays.asList(new ChatMessage(userName, messageString, UserFilter.NO_FILTER)));
							return event;
						}
					}),
					new Pair<>(BATTLE_ROOM_USERS_ADDRESS,  new ObjectBuilderWithParam() {
						@Override
						public Object build() {
							String userName = (String) getParam();
							return new UserEvent(new Date(), TimeEventType.USER_LEAVE, Arrays.asList(userName));
						}
					})
			});
		}});

		private static SimpMessagingTemplate simpMessagingTemplate;
		private static ChatLogManager chatLogManager;

		private Map<ActionType, Pair<String, ObjectBuilderWithParam>[]> brokerListeners;

		private PersistentConnection(Map<ActionType, Pair<String, ObjectBuilderWithParam>[]> brokerListeners) {
			this.brokerListeners = brokerListeners;
		}

		public void notifyListeners(ActionType actionType, Object builderParam) {
			Pair<String, ObjectBuilderWithParam>[] actionParams = brokerListeners.get(actionType);
			for (Pair<String, ObjectBuilderWithParam> actionParam : actionParams) {
				ObjectBuilder builder = actionParam.getSecond().cloneWithParam(builderParam);
				new SendWebsocketMessage(simpMessagingTemplate, actionParam.getFirst(), builder).make();
			}
		}

		public static void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
			PersistentConnection.simpMessagingTemplate = simpMessagingTemplate;
		}

		public static void setChatLogManager(ChatLogManager chatLogManager) {
			PersistentConnection.chatLogManager = chatLogManager;
		}
	}

	private class Place {
		private Map<String, Set<String>> userSessions = new HashMap<>();
		private UserService userService = new MemoryUserService();

		public Map<String, Set<String>> getUserSessions() {
			return userSessions;
		}

		public UserService getUserService() {
			return userService;
		}
	}

	private enum ActionType {
		ADD,
		REMOVE
	}
}
