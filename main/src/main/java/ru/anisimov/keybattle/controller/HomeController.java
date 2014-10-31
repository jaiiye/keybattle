package ru.anisimov.keybattle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.anisimov.keybattle.model.text.TokenizedText;
import ru.anisimov.keybattle.model.text.TokenizedTextManager;

import static ru.anisimov.keybattle.config.DestinationConfig.*;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/7/14
 */
@Controller
@RequestMapping(value = INDEX_ADDRESS)
public class HomeController {
	@Autowired
	private TokenizedTextManager tokenizedTextManager;

	@RequestMapping(method = RequestMethod.GET)
	public String getHomepage(Model model) {
		TokenizedText text = tokenizedTextManager.getRandomText();
		model.addAttribute("text", text);
		return HOME_ADDRESS;
	}
}
