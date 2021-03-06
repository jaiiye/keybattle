package ru.anisimov.keybattle.security.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.anisimov.keybattle.data.service.interfaces.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/16/14
 */
public class LoginValidator implements ConstraintValidator<UniqueLogin, String> {
	@Autowired
	public UserService userService;

	@Override
	public void initialize(UniqueLogin constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return userService.getUserByUserName(value) == null;
	}
}
