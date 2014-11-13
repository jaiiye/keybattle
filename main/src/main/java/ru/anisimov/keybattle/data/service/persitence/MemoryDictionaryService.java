package ru.anisimov.keybattle.data.service.persitence;

import ru.anisimov.keybattle.model.locale.Dictionary;
import ru.anisimov.keybattle.model.locale.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/7/14
 */
public class MemoryDictionaryService {
	private Map<Integer, Dictionary> dictionaryById;
	private Map<Integer, List<Dictionary>> wordDictionaries;
	private Map<Long, Dictionary> termIdDictionary;

	public MemoryDictionaryService(Map<Integer, Dictionary> dictionaryById) {
		this.dictionaryById = dictionaryById;
		wordDictionaries = new HashMap<>();
		termIdDictionary = new HashMap<>();
		for (Integer dictId : dictionaryById.keySet()) {
			dictionaryById.get(dictId).getTerms().forEach(term -> {
				Dictionary dictionary = dictionaryById.get(dictId);
				term.values().forEach(value -> {
					int hash = value.hashCode();
					if (!wordDictionaries.containsKey(hash)) {
						wordDictionaries.put(hash, new ArrayList<>());
					}
					wordDictionaries.get(hash).add(dictionary);
				});
				termIdDictionary.put(term.getId(), dictionary);
			});
		}
	}

	public String getTranslation(int dictType, long termId, String locale) {
		Dictionary dictionary = dictionaryById.get(dictType);
		return  dictionary == null ? null : dictionary.getTranslation(termId, locale);
	}

	public Term getTerm(String value) {
		List<Dictionary> dictionaries = wordDictionaries.get(value.hashCode());
		if (dictionaries == null || dictionaries.size() == 0) {
			return null;
		}
		List<Term> terms = dictionaries.stream()
				.map(dictionary -> dictionary.getTerm(value))
				.filter(term -> term != null)
				.collect(Collectors.toList());
		return terms != null && terms.size() > 0 ? terms.get(0) : null;
	}

	public Term getTerm(long termId) {
		Dictionary dictionary = termIdDictionary.get(termId);
		if (dictionary == null) {
			return null;
		}
		Term result = dictionary.getTerm(termId);
		return result == null ? null : result;
	}
}
