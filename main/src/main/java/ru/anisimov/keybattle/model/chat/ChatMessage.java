package ru.anisimov.keybattle.model.chat;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/20/14
 */
public class ChatMessage {
	private String userName;
	private String text;
	private UserFilter recipients;

	public ChatMessage() {
	}

	public ChatMessage(String userName, String text, UserFilter recipients) {
		this.userName = userName;
		this.text = text;
		this.recipients = recipients;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public UserFilter getRecipients() {
		return recipients;
	}

	public void setRecipients(UserFilter recipients) {
		this.recipients = recipients;
	}
}
