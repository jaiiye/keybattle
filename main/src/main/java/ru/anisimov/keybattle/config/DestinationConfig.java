package ru.anisimov.keybattle.config;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/22/14
 */
public interface DestinationConfig {
	String INDEX_ADDRESS = "/";
	String HOME_ADDRESS = "/home";

	String RESOURCES_ADDRESS = "/resources/";

	String LOGIN_ADDRESS = "/login";
	String LOGIN_ERROR_ADDRESS = "/login?error=login_error";
	String SIGN_UP_ADDRESS = "/sign-up";
	String SIGN_UP_SUCCESS_ADDRESS = "/login?signup=success";

	String BATTLE_ROOM_ADDRESS = "/battle/room";
	String BATTLE_ROOM_CHAT_ADDRESS = "/battle/room/chat";
	String BATTLE_ROOM_USERS_ADDRESS = "/battle/room/users";
	String BATTLE_ROOM_USER_LIST_ADDRESS = "/battle/room/userList";
	String BATTLE_ROOM_MESSAGES_ADDRESS = "/battle/room/messages";
	String BATTLE_ROOM_MESSAGE_LIST_ADDRESS = "/battle/room/messageList";
}
