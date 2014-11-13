package ru.anisimov.keybattle.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import ru.anisimov.keybattle.model.locale.LocaleStrings;
import ru.anisimov.keybattle.model.params.Params;

import static ru.anisimov.keybattle.config.DestinationConfig.LOGIN_ADDRESS;
import static ru.anisimov.keybattle.config.DestinationConfig.RESOURCES_ADDRESS;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/9/14
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ru.anisimov.keybattle.controller"})
public class WebConfig extends WebMvcConfigurerAdapter {
	private static final String TEMPLATES_FOLDER = "/pages";
	private static final String TEMPLATES_EXTENSION = ".html";
	private static final String TEMPLATES_MODE = "HTML5";
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final String[] MESSAGES_PLACES = {
			"classpath:messages/messages", 
			"classpath:messages/validation"
	};

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController(LOGIN_ADDRESS).setViewName(LOGIN_ADDRESS);
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(RESOURCES_ADDRESS + "**").addResourceLocations(RESOURCES_ADDRESS);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName(Params.LANG);
		registry.addInterceptor(localeChangeInterceptor);
	}

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
		cookieLocaleResolver.setDefaultLocale(StringUtils.parseLocaleString(LocaleStrings.DEFAULT));
		return cookieLocaleResolver;
	}

	@Bean
	public ServletContextTemplateResolver templateResolver() {
		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
		resolver.setPrefix(TEMPLATES_FOLDER);
		resolver.setSuffix(TEMPLATES_EXTENSION);

		//NB, selecting HTML5 as the template mode.
		resolver.setCharacterEncoding(DEFAULT_ENCODING);
		resolver.setTemplateMode(TEMPLATES_MODE);
		resolver.setCacheable(false);
		return resolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine(MessageSource messageSource, ServletContextTemplateResolver templateResolver) {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver);
		engine.setMessageSource(messageSource);
		engine.addDialect(new SpringSecurityDialect());
		return engine;
	}

	@Bean
	public ViewResolver viewResolver(SpringTemplateEngine templateEngine) {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine);
		viewResolver.setOrder(1);
		viewResolver.setViewNames(new String[]{"*"});
		viewResolver.setCache(false);
		viewResolver.setCharacterEncoding(DEFAULT_ENCODING);
		return viewResolver;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames(MESSAGES_PLACES);
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setDefaultEncoding(DEFAULT_ENCODING);
		messageSource.setCacheSeconds(0);
		return messageSource;
	}
}
