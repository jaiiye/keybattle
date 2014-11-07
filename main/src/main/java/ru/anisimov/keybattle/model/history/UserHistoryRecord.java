package ru.anisimov.keybattle.model.history;

import ru.anisimov.keybattle.core.HasId;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/31/14
 */
public class UserHistoryRecord implements HasId {
	private long id;
	private long userId;
	private int actionId;
	private String actionName;
	private String actionDescription;
	private LocalDateTime creationTime;
	private long actorId;

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public long getActorId() {
		return actorId;
	}

	public void setActorId(long actorId) {
		this.actorId = actorId;
	}
	
	private Object[] keyArray() {
		return new Object[]{id, userId, actionId, actionName, actionDescription, creationTime, actorId};
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(keyArray());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UserHistoryRecord)) {
			return false;
		}
	
		UserHistoryRecord that = (UserHistoryRecord) obj;
		return Arrays.equals(this.keyArray(), that.keyArray());
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("UserHistoryRecord: ")
				.append(Arrays.toString(keyArray()))
				.toString();
	}
}
