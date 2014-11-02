package ru.anisimov.keybattle.model.user;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.ScriptAssert;
import ru.anisimov.keybattle.security.validation.UniqueEmail;
import ru.anisimov.keybattle.security.validation.UniqueLogin;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/14/14
 */
@ScriptAssert(lang = "javascript", script = "_this.confirmPassword == _this.password")
public class Registration {
	@NotNull
	@NotEmpty
	@Size(min = 4, max = 20)
	@Pattern(regexp = "^(?=\\S+$)[a-zA-Z]+[a-zA-Z0-9_-]+$")
	@UniqueLogin
	private String userName;

	@NotNull
	@NotEmpty
	@Size(min = 6, max = 20)
	@Pattern(regexp = "^(?=\\S+$)[a-zA-Z0-9\\p{Punct}]+$")
	private String password;

	private String confirmPassword;

	@NotNull
	@NotEmpty
	@Email
	@UniqueEmail
	private String email;

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}

