package ru.anisimov.keybattle.config;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import ru.anisimov.keybattle.model.text.Text;
import ru.anisimov.keybattle.service.interfaces.TextService;
import ru.anisimov.keybattle.service.persitence.MemoryTextService;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/7/14
 */
@Configuration
@ComponentScan(basePackages = {"ru.anisimov.keybattle.service", "ru.anisimov.keybattle.model"})
public class CoreConfig {
	@Bean
	public TextService textService() {
		class FileTextLoader {
			private static final String FILE_NAME = "/home/valter/texts.txt";
			private static final String SEPARATOR = "-===-";

			public List<Text> loadTexts() {
				List<Text> result = new ArrayList<>();

				try (BufferedReader in = new BufferedReader(new FileReader(FILE_NAME))) {
					String line;
					int textInd = 0;
					StringBuilder currentText = new StringBuilder();
					while ((line = in.readLine()) != null) {
						if (SEPARATOR.equals(line.trim())) {
							result.add(new Text(textInd++, currentText.toString().trim()));
							currentText = new StringBuilder();
						} else {
							currentText.append(line.trim()).append(" ");
						}
					}
				} catch (IOException e) {
				}

				return result;
			}
		}

		return new MemoryTextService(new FileTextLoader().loadTexts());
	}

	@Bean
	public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
		return new TransactionTemplate(transactionManager);
	}

	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	public ReCaptcha reCaptcha() {
		ReCaptchaImpl result = new ReCaptchaImpl();
		result.setPrivateKey("6LeuzPsSAAAAAMFl5jh5-z8D6iizUl2RqIXx5t-u");
		return result;
	}
}
