package ru.anisimov.keybattle.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.anisimov.keybattle.action.SendToUserWebsocketMessage;
import ru.anisimov.keybattle.action.SendWebsocketMessage;
import ru.anisimov.keybattle.exception.ApplicationError;
import ru.anisimov.keybattle.exception.ApplicationErrorType;
import ru.anisimov.keybattle.manager.ChatLogManager;
import ru.anisimov.keybattle.manager.UsersOnlineManager;
import ru.anisimov.keybattle.model.chat.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ru.anisimov.keybattle.config.DestinationConfig.*;
import static ru.anisimov.keybattle.manager.UsersOnlineManager.PersistentConnection.BATTLE_ROOM;
import static ru.anisimov.keybattle.model.chat.TimeEventType.*;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/20/14
 */
@Controller
public class BattleRoomController {
	private static final Logger log = Logger.getLogger(BattleRoomController.class);

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private ChatLogManager chatLogManager;
	@Autowired
	private UsersOnlineManager usersOnlineManager;

	@RequestMapping(value = BATTLE_ROOM_ADDRESS, method = RequestMethod.GET)
	public String showRoom(Model model) {
		return BATTLE_ROOM_ADDRESS;
	}

	@MessageMapping(BATTLE_ROOM_USER_LIST_ADDRESS)
	@SendTo(BATTLE_ROOM_USERS_ADDRESS)
	public UserEvent getUsers() throws Exception {
		return new UserEvent(new Date(), USER_LIST, usersOnlineManager.getUsers(BATTLE_ROOM));
	}

	@MessageMapping(BATTLE_ROOM_MESSAGE_LIST_ADDRESS)
	@SendToUser(value = BATTLE_ROOM_CHAT_ADDRESS, broadcast = false)
	public MultiChatEvent getLog(UserFilter userFilter, Principal principal) throws Exception {
		List<ChatEvent> log = chatLogManager.getLog(principal.getName(), userFilter);
		return new MultiChatEvent(new Date(), CHAT_LIST,
				log.stream()
						.sorted((event1, event2) -> event2.getEventtime().compareTo(event1.getEventtime()))
						.collect(Collectors.toList())
		);
	}

	@MessageMapping(BATTLE_ROOM_MESSAGES_ADDRESS)
	public void userSay(ChatMessage message, Principal principal) throws Exception {
		UserFilter recipients = message.getRecipients();
		ChatEvent event = new ChatEvent(new Date(), CHAT_MESSAGE,
				Arrays.asList(new ChatMessage(principal.getName(), message.getText(), recipients))
		);
		sendMessage(event, BATTLE_ROOM_CHAT_ADDRESS, recipients.getUsers());
		chatLogManager.add(event);
	}

	@MessageExceptionHandler
	@SendToUser(value= BATTLE_ROOM_CHAT_ADDRESS, broadcast=false)
	public ApplicationError handleException(Exception exception) {
		return new ApplicationError("", ApplicationErrorType.ALERT);
	}

	private void sendMessage(Object event, String destination, List<String> userNames) {
		if (userNames == null || userNames.size() == 0) {
			new SendWebsocketMessage(template, destination, () -> event).make();
		} else {
			for (String userName : userNames) {
				new SendToUserWebsocketMessage(template, destination, userName, () -> event).make();
			}
		}
	}

	private class ChatMessageException extends Exception {
		public ChatMessageException(Throwable cause) {
			super(cause);
		}
	}

	private class UserListException extends Exception {
		public UserListException(Throwable cause) {
			super(cause);
		}
	}

	private class ChatLogException extends Exception {
		public ChatLogException(Throwable cause) {
			super(cause);
		}
	}
}
