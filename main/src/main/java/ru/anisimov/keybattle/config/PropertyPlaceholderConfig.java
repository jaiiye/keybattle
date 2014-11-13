package ru.anisimov.keybattle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/12/14
 */
@Configuration
@PropertySource("classpath:jdbc.properties")
public class PropertyPlaceholderConfig {
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
