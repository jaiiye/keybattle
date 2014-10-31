package ru.anisimov.keybattle.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.anisimov.keybattle.service.interfaces.UserService;

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
		return userService.getByEmail(value) == null;
	}
}
