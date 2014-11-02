package ru.anisimov.keybattle.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import ru.anisimov.keybattle.security.filter.AntiRobotFilter;

import javax.sql.DataSource;

import static ru.anisimov.keybattle.config.DestinationConfig.*;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/10/14
 */
@EnableWebSecurity
@Configuration
@ComponentScan(basePackages = {"ru.anisimov.keybattle.security.filter"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private AntiRobotFilter antiRobotFilter;

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.jdbcAuthentication()
					.dataSource(dataSource)
					.usersByUsernameQuery("select username, password, enabled from user where username=?")
					.authoritiesByUsernameQuery("select u.username, r.role from user_role r left join user u on u.id = r.user_id where u.username=?")
		;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.headers()
					.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
					.and()
				.authorizeRequests()
					.antMatchers(RESOURCES_ADDRESS + "**").permitAll()
					.antMatchers(LOGIN_ADDRESS + "*").permitAll()
					.antMatchers(SIGN_UP_ADDRESS + "*").permitAll()
					.anyRequest().authenticated()
					.and()
				.formLogin()
					.loginPage(LOGIN_ADDRESS).permitAll()
					.failureUrl(LOGIN_ERROR_ADDRESS)
					.and()
				.logout().permitAll()
					.and()
				.addFilterAfter(antiRobotFilter, FilterSecurityInterceptor.class)
		;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
