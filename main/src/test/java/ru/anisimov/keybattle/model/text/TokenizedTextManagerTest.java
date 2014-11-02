package ru.anisimov.keybattle.model.text;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import ru.anisimov.keybattle.data.service.interfaces.TextService;
import ru.anisimov.keybattle.data.service.persitence.MemoryTextService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

public class TokenizedTextManagerTest {
	private static final List<Text> TEST_TEXTS = Arrays.asList(
		new Text(0, "Will you send me an angel?"),
		new Text(1, " как На наши  imeniny испекли     мы каравай, каравай, каравай!  "),
		new Text(2, "Однозначно")
	);

	private static final Map<Long, List<Token>> EXPECTED_TOKENS = new HashMap<Long, List<Token>>() {{
		put(0l, Arrays.asList(new Token(0, "Will"), new Token(1, "you"), new Token(2, "send"), new Token(3, "me"), new Token(4, "an"), new Token(5, "angel?")));
		put(1l, Arrays.asList(new Token(0, "как"), new Token(1, "На"), new Token(2, "наши"), new Token(3, "imeniny"), new Token(4, "испекли"), new Token(5, "мы"), new Token(6, "каравай,"), new Token(7, "каравай,"), new Token(8, "каравай!")));
		put(2l, Arrays.asList(new Token(0, "Однозначно")));
	}};

	@InjectMocks
	TokenizedTextManager tokenizedTextManager;

	@Mock
	TextService textService;

	@Spy
	TokenizationUtil tokenizationUtil;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		MemoryTextService memoryTextService = new MemoryTextService(TEST_TEXTS);
		when(textService.getAllTexts()).thenReturn(memoryTextService.getAllTexts());
		when(textService.getRandomText()).thenReturn(memoryTextService.getRandomText());
		when(textService.getTextById(anyLong())).thenReturn(Text.NULL_TEXT);
		for (int i = 0; i < TEST_TEXTS.size(); i++) {
			Text text = TEST_TEXTS.get(i);
			when(textService.getTextById(text.getId())).thenReturn(memoryTextService.getTextById(text.getId()));
		}
	}

	@Test
	public void testGetRandomText() throws Exception {
		TokenizedText randomText = tokenizedTextManager.getRandomText();
		assertTrue(TEST_TEXTS.stream()
				.anyMatch(text -> text.getId() == randomText.getId()));
		assertEquals(EXPECTED_TOKENS.get(randomText.getId()), randomText.getTokens());
	}

	@Test
	public void testGetTextById() throws Exception {
		for (int i = 0; i < TEST_TEXTS.size(); i++) {
			TokenizedText tokenizedText = tokenizedTextManager.getTextById(i);
			assertEquals(i, tokenizedText.getId());
			assertEquals(EXPECTED_TOKENS.get(tokenizedText.getId()), tokenizedText.getTokens());
		}
		TokenizedText tokenizedText = tokenizedTextManager.getTextById(-1);
		assertEquals(Text.NULL_TEXT.getId(), tokenizedText.getId());
	}

	@Test
	public void testGetAllTexts() throws Exception {
		List<TokenizedText> tokenizedTexts = tokenizedTextManager.getAllTexts();
		assertEquals(TEST_TEXTS.size(), tokenizedTexts.size());
	}
}
