package ru.anisimov.keybattle.data.service.interfaces;

import ru.anisimov.keybattle.model.locale.Term;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/7/14
 */
public interface DictionaryService {
	Term getTerm(long termId);

	Term getTerm(String value);

	boolean addTerm(int dictType, Term term);

	boolean changeTerm(int dictType, Term term);

	boolean removeTerm(long termId);
	
	String getTranslation(int dictType, long termId, String locale);
}
