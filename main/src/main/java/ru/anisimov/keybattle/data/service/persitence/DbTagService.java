package ru.anisimov.keybattle.data.service.persitence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import ru.anisimov.keybattle.data.service.interfaces.DictionaryService;
import ru.anisimov.keybattle.data.service.interfaces.TagService;
import ru.anisimov.keybattle.model.locale.Term;
import ru.anisimov.keybattle.model.tag.Tag;

import java.util.List;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/10/14
 */
@Service("tagService")
public class DbTagService implements TagService {
	private static final String SELECT_TAG_BY_ID = "SELECT T.ID, T.TERM_ID, D.LOCALE, D.VALUE FROM TAG T " +
			" JOIN DICT D ON (T.TERM_ID = D.TERM_ID) WHERE T.DELETED = 0 AND T.ID = ?";

	private static final String SELECT_TAG_BY_NAME = "SELECT T.ID, T.TERM_ID, D.LOCALE, D.VALUE FROM TAG T " +
			" JOIN DICT D ON (T.TERM_ID = D.TERM_ID) WHERE T.DELETED = 0 AND T.TERM_ID = " +
			" (SELECT TERM_ID FROM DICT WHERE VALUE = ?)";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private DictionaryService dictionaryService;
	
	@Override
	public Tag getTag(long id) {
		Tag result = new Tag(id, new Term());
		Term tagName = result.getName();
		jdbcTemplate.query(SELECT_TAG_BY_ID, (RowCallbackHandler)rs -> {
			tagName.setId(rs.getLong("TERM_ID"));
			tagName.put(rs.getString("LOCALE"), rs.getString("VALUE"));
		}, id);
		return tagName.size() > 0 ? result : null;
	}

	@Override
	public Tag getTag(String tagName) {
		Tag result = new Tag();
		Term tagNameTerm = new Term();
		result.setName(tagNameTerm);
		jdbcTemplate.query(SELECT_TAG_BY_NAME, (RowCallbackHandler)rs -> {
			tagNameTerm.setId(rs.getLong("TERM_ID"));
			tagNameTerm.put(rs.getString("LOCALE"), rs.getString("VALUE"));
		}, tagName);
		return tagNameTerm.size() > 0 ? result : null;
	}

	@Override
	public List<Tag> getTags() {
		return null;
	}

	@Override
	public boolean addTag(String tagName) {
		return false;
	}

	@Override
	public boolean addTag(Term term) {
		return false;
	}

	@Override
	public boolean removeTag(long tagId) {
		return false;
	}
}
