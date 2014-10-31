package ru.anisimov.keybattle.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import ru.anisimov.keybattle.service.remote.AntiRobotService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/15/14
 */
@Component
public class AntiRobotFilter extends GenericFilterBean {
	private static final String CAPTCHA_CHALLENGE_FIELD = "recaptcha_challenge_field";
	private static final String CAPTCHA_RESPONSE_FIELD = "recaptcha_response_field";

	private static final String RECAPTCHA_ERROR = "error=recaptcha_error";

	@Autowired
	private AntiRobotService antiRobotService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		String challengeParam = request.getParameter(CAPTCHA_CHALLENGE_FIELD);
		if (!StringUtils.isEmpty(challengeParam)) {
			String remoteAddress = request.getRemoteAddr();
			String responseParam = request.getParameter(CAPTCHA_RESPONSE_FIELD);
			if (!StringUtils.isEmpty(responseParam)) {
				AntiRobotService.ValidationParams params = new AntiRobotService.ValidationParams(remoteAddress, challengeParam, responseParam);
				if (antiRobotService.valid(params)) {
					filterChain.doFilter(request, response);
					return;
				}
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher(getErrorUrl((HttpServletRequest)request));
			dispatcher.forward(request, response);
			return;
		}
		filterChain.doFilter(request, response);
	}

	private String getErrorUrl(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		String queryString = request.getQueryString();
		return servletPath + (StringUtils.isEmpty(queryString) ? "?" : "&") + RECAPTCHA_ERROR;
	}
}
