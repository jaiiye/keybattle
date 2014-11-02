package ru.anisimov.keybattle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.anisimov.keybattle.model.user.UserBuilder;
import ru.anisimov.keybattle.model.user.Registration;
import ru.anisimov.keybattle.data.service.interfaces.UserService;

import javax.validation.Valid;

import static ru.anisimov.keybattle.config.DestinationConfig.*;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/14/14
 */
@Controller
@RequestMapping(SIGN_UP_ADDRESS)
public class RegistrationController {
	private static final String RECAPTCHA_ERROR = "recaptcha_error";

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public String showRegistration(Model model) {
		model.addAttribute("registration", new Registration());
		return SIGN_UP_ADDRESS;
	}

	@RequestMapping(method = RequestMethod.POST, params = {"error"})
	public String processRegistrationErrors(@RequestParam(value = "error") String error, Registration registration, Model model) {
		if (RECAPTCHA_ERROR.equals(error)) {
			model.addAttribute("invalidRecaptcha", true);
		}
		return SIGN_UP_ADDRESS;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String processRegistration(@Valid Registration registration, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return SIGN_UP_ADDRESS;
		}

		userService.addUser(new UserBuilder(registration)
				.addRole("USER")
				.build()
		);
		return "redirect:" + SIGN_UP_SUCCESS_ADDRESS;
	}
}
