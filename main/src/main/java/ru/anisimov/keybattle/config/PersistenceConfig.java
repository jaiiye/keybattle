package ru.anisimov.keybattle.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/10/14
 */
@Configuration
@Import(PropertyPlaceholderConfig.class)
public class PersistenceConfig {
	@Bean
	public DataSource dataSource(
			@Value("${main.jdbc.driver}") String driver,
			@Value("${main.jdbc.url}") String url,
			@Value("${main.jdbc.username}") String username,
			@Value("${main.jdbc.password}") String password) {
		DriverManagerDataSource result = new DriverManagerDataSource();
		result.setDriverClassName(driver);
		result.setUrl(url);
		result.setUsername(username);
		result.setPassword(password);
		return result;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource, true);
	}
}
