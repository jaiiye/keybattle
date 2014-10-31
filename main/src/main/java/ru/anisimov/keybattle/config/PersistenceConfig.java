package ru.anisimov.keybattle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/10/14
 */
@Configuration
public class PersistenceConfig {
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource result = new DriverManagerDataSource();
		result.setDriverClassName("com.mysql.jdbc.Driver");
		result.setUrl("jdbc:mysql://localhost:3306/keybattle");
		result.setUsername("root");
		result.setPassword("[jvzr;bd`n4ujlf");
		return result;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource, true);
	}
}
