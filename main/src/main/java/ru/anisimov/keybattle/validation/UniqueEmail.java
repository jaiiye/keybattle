package ru.anisimov.keybattle.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/16/14
 */
@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
	String message() default "{ru.anisimov.keybattle.validation.UniqueEmail.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
