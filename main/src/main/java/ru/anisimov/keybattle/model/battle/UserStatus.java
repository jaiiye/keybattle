package ru.anisimov.keybattle.model.battle;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/29/14
 */
public class UserStatus {
	private String userName;
	private int currentWord;
	private int points;
	private boolean finished;

	public UserStatus() {}

	public UserStatus(String userName) {
		this(userName, 0, 0, false);
	}

	public UserStatus(String userName, int currentWord, int points, boolean finished) {
		this.userName = userName;
		this.currentWord = currentWord;
		this.points = points;
		this.finished = finished;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getCurrentWord() {
		return currentWord;
	}

	public void setCurrentWord(int currentWord) {
		this.currentWord = currentWord;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
