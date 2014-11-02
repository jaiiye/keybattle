package ru.anisimov.keybattle.websocket.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import ru.anisimov.keybattle.data.manager.UsersOnlineManager;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.anisimov.keybattle.config.DestinationConfig.*;
import static ru.anisimov.keybattle.data.manager.UsersOnlineManager.PersistentConnection.BATTLE_ROOM;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/22/14
 */
public class UserOnlineInterceptor extends ChannelInterceptorAdapter {
	private static final Map<String, UsersOnlineManager.PersistentConnection> INTERCEPT_URLS =
			new HashMap<String, UsersOnlineManager.PersistentConnection>() {{
		put(BATTLE_ROOM_ADDRESS, BATTLE_ROOM);
		put(BATTLE_ROOM_CHAT_ADDRESS, BATTLE_ROOM);
		put(BATTLE_ROOM_USERS_ADDRESS, BATTLE_ROOM);
	}};

	private static final Map<String, String> sessionUser = new ConcurrentHashMap<>();

	private UsersOnlineManager usersOnlineManager;

	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
		if (!sent) {
			return;
		}

		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		Principal user = accessor.getUser();
		StompCommand command = accessor.getCommand();
		String sessionId = accessor.getSessionId();
		String url = accessor.getDestination();

		switch(command.getMessageType()) {
			case DISCONNECT:
				if (sessionUser.containsKey(sessionId)) {
					String userName = sessionUser.get(sessionId);
					usersOnlineManager.removeUserSession(userName, sessionId);
					sessionUser.remove(sessionId);
				}
				break;
			case CONNECT:
			case SUBSCRIBE:
			case MESSAGE:
			case HEARTBEAT:
				if (user != null && !sessionUser.containsKey(sessionId) && INTERCEPT_URLS.containsKey(url)) {
					sessionUser.put(sessionId, user.getName());
					usersOnlineManager.addUserSession(user.getName(), sessionId, INTERCEPT_URLS.get(url));
				}
				break;
		}
	}

	public void setUsersOnlineManager(UsersOnlineManager usersOnlineManager) {
		this.usersOnlineManager = usersOnlineManager;
	}
}
