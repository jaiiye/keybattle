package ru.anisimov.keybattle.data.service.interfaces;

import ru.anisimov.keybattle.model.locale.Term;
import ru.anisimov.keybattle.model.tag.Tag;

import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/6/14
 */
public interface TagService {
	Tag getTag(long id);
	
	Tag getTag(String tagName);
	
	List<Tag> getTags();

	boolean addTag(String tagName);
	
	boolean addTag(Term term);

	boolean removeTag(long tagId);
}
