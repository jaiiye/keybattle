package ru.anisimov.keybattle.model.locale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/7/14
 */
public class Dictionary {
	private Map<Long, Term> termById;
	private Map<String, Term> termByValue;

	public Dictionary() {
		termById = new HashMap<>();
		termByValue = new HashMap<>();
	}

	public boolean hasTerm(long termId) {
		return termById.containsKey(termId);
	}

	public boolean hasTerm(String value) {
		return termByValue.containsKey(value);
	}

	public Term getTerm(long termId) {
		return termById.get(termId);
	}

	public Term getTerm(String value) {
		return termByValue.get(value);
	}

	public List<Term> getTerms() {
		return new ArrayList<>(termById.values());
	}

	public void addTerm(long termId, Term term) {
		termById.put(termId, term);
		term.values().forEach(value -> termByValue.put(value, term));
	}

	public boolean removeTerm(long termId) {
		Term term = termById.get(termId);
		if (term == null) {
			return false;
		}
		termById.remove(termId);
		term.values().forEach(termByValue::remove);
		return true;
	}

	public boolean removeTerm(String value) {
		Term term = termByValue.get(value);
		if (term == null) {
			return false;
		}
		termById.remove(term.getId());
		term.values().forEach(termByValue::remove);
		return true;
	}
	
	public String getTranslation(long termId, String locale) {
		Term term = termById.get(termId);
		return term == null ? null : term.get(locale);
	}
}
