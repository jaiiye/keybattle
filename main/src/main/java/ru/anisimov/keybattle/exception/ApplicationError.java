package ru.anisimov.keybattle.exception;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/28/14
 */
public class ApplicationError {
	private Object message;
	private ApplicationErrorType type;

	public ApplicationError(Object message, ApplicationErrorType type) {
		this.message = message;
		this.type = type;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public ApplicationErrorType getType() {
		return type;
	}

	public void setType(ApplicationErrorType type) {
		this.type = type;
	}
}
