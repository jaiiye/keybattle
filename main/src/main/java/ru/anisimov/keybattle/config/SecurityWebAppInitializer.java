package ru.anisimov.keybattle.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/10/14
 */
@Order(2)
public class SecurityWebAppInitializer  extends AbstractSecurityWebApplicationInitializer {
}
