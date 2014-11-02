package ru.anisimov.keybattle.security.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/15/14
 */
@Documented
@Constraint(validatedBy = LoginValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueLogin {
	String message() default "{ru.anisimov.keybattle.validation.UniqueLogin.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
