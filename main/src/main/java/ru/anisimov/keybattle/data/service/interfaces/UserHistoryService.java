package ru.anisimov.keybattle.data.service.interfaces;

import ru.anisimov.keybattle.model.history.UserHistoryRecord;

import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/31/14
 */
public interface UserHistoryService {
	boolean addHistoryRecord(long userId, int actionId, long actorId);

	boolean addHistoryRecord(String userName, int actionId, long actorId);

	List<UserHistoryRecord> getRecordsByUserAndAction(long userId, int actionId);

	List<UserHistoryRecord> getRecordsByUserAndAction(String userName, int actionId);

	List<UserHistoryRecord> getRecordsByUser(long userId);

	List<UserHistoryRecord> getRecordsByUser(String userName);

	List<UserHistoryRecord> getRecordsByActor(long actorId);
}
