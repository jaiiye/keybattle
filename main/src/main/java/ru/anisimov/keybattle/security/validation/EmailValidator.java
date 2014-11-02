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
public class EmailValidator implements ConstraintValidator<UniqueEmail, String> {
	@Autowired
	public UserService userService;

	@Override
	public void initialize(UniqueEmail constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return userService.getUserByEmail(value) == null;
	}
}
