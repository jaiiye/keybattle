package ru.anisimov.keybattle.data.service.persitence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import ru.anisimov.keybattle.core.ObjectCache;
import ru.anisimov.keybattle.data.service.interfaces.DictionaryService;
import ru.anisimov.keybattle.model.locale.Dictionary;
import ru.anisimov.keybattle.model.locale.Term;
import ru.anisimov.keybattle.util.db.LongRowMapper;
import ru.anisimov.keybattle.util.db.SqlUtil;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/7/14
 */
@Service("dictionaryService")
public class DbDictionaryService implements DictionaryService {
	private static final long DEFAULT_EXPIRATION_TIME = 1000 * 60 * 10; // 10 min

	private static final String SELECT_NEXT_TERM_ID = "SELECT IFNULL(MAX(TERM_ID), 0) + 1 TID FROM DICT";
	
	private static final String ADD_TERM = "INSERT INTO DICT (TYPE_ID, TERM_ID, LOCALE, VALUE) VALUES (?, ?, ?, ?)";
	
	private static final String REMOVE_TERM = "DELETE FROM DICT WHERE TERM_ID = ?";
	
	private static final String SELECT_DICTIONARIES = "SELECT * FROM DICT";
	
	private static final String SELECT_TERM_ID_FROM_DICT = "SELECT TERM_ID FROM DICT WHERE VALUES IN ";

	private ObjectCache<MemoryDictionaryService> memoryCache;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void init() {
		memoryCache = new ObjectCache<>(
				() -> new MemoryDictionaryService(loadDictionaries()),
				DEFAULT_EXPIRATION_TIME
		);
	}
	
	private Map<Integer, Dictionary> loadDictionaries() {
		final Map<Integer, Dictionary> result = new HashMap<>();
		jdbcTemplate.query(SELECT_DICTIONARIES, (RowCallbackHandler)rs -> {
			int dictType = rs.getInt("TYPE_ID");
			if (!result.containsKey(dictType)) {
				result.put(dictType, new Dictionary());
			}
			
			long termId = rs.getLong("TERM_ID");
			Dictionary dictionary = result.get(dictType);
			if (!dictionary.hasTerm(termId)) {
				dictionary.addTerm(termId, new Term());
			}
			dictionary.getTerm(termId).put(rs.getString("LOCALE"), rs.getString("VALUE"));
		});
		return result;
	}
	
	@Override
	public boolean addTerm(int dictType, Term term) {
		final long nextTermId = getNextTermId();
		if (existsInDict(new ArrayList<>(term.values()))) {
			return false;
		}
		List<Map.Entry<String, String>> entries = new ArrayList<>(term.entrySet());
		if (!Arrays.stream(jdbcTemplate.batchUpdate(ADD_TERM, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, dictType);
					ps.setLong(2, nextTermId);
					ps.setString(3, entries.get(i).getKey());
					ps.setString(4, entries.get(i).getValue());
				}
	
				@Override
				public int getBatchSize() {
					return term.size();
				}
			}))
				.anyMatch(i -> i == 0)) {
			memoryCache.buildObject();
			return true;
		}
		return false;
	}
	
	private boolean existsInDict(List<String> values) {
		List<Long> result = jdbcTemplate.query(SELECT_TERM_ID_FROM_DICT  
				+ SqlUtil.inBlock(values.toArray(new String[values.size()])), new LongRowMapper());
		return result != null && result.size() > 0;
	}
	
	private long getNextTermId() {
		return jdbcTemplate.query(SELECT_NEXT_TERM_ID, new LongRowMapper()).get(0);
	}

	@Override
	public boolean changeTerm(int dictType, Term term) {
		if (removeTerm(term.getId()) && addTerm(dictType, term)) {
			memoryCache.buildObject();
			return true;
		}
		return false;
	}

	@Override
	public boolean removeTerm(long termId) {
		if (jdbcTemplate.update(REMOVE_TERM, termId) != 0) {
			memoryCache.buildObject();
			return true;
		}
		return false;
	}

	@Override
	public String getTranslation(int dictType, long termId, String locale) {
		return memoryCache.get().getTranslation(dictType, termId, locale);
	}

	@Override
	public Term getTerm(String value) {
		return memoryCache.get().getTerm(value);
	}

	@Override
	public Term getTerm(long termId) {
		return memoryCache.get().getTerm(termId);
	}
}
