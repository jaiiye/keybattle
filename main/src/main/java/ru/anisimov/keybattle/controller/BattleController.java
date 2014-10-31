package ru.anisimov.keybattle.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.anisimov.keybattle.model.battle.UserStatus;

import java.security.Principal;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/29/14
 */
@Controller
public class BattleController {
	@MessageMapping("")
	@SendTo("")
	public UserStatus updateStatus(UserStatus status, Principal user) {
		status.setUserName(user.getName());
		return status;
	}


	@MessageMapping("")
	@SendTo("")
	public UserStatus updateStatus(UserStatus status, Principal user) {
		status.setUserName(user.getName());
		return status;
	}
}
