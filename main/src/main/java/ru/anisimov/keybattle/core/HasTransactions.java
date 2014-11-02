package ru.anisimov.keybattle.core;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/2/14
 */
public interface HasTransactions {
	Logger log = Logger.getLogger(HasTransactions.class);
	
	default boolean doInTransactionWithoutException(Action action, String errorMessage) {
		return getTransactionTemplate().execute((TransactionStatus status) -> {
			try {
				boolean success = action.make();
				if (!success) {
					status.setRollbackOnly();
				}
				return success;
			} catch (Exception e) {
				log.error(errorMessage, e);
				status.setRollbackOnly();
				return false;
			}
		});
	}

	default <T> T getWithoutException(Getter<T> getter, String errorMessage) {
		try {
			return getter.get();
		} catch (Exception e) {
			log.error(errorMessage, e);
			return null;
		}
	}
	
	TransactionTemplate getTransactionTemplate();
	
	interface Action {
		boolean make();
	}

	interface Getter<T> {
		T get();
	}
}
