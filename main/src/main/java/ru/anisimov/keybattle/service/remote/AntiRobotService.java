package ru.anisimov.keybattle.service.remote;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/16/14
 */
@Service
public class AntiRobotService {
	@Autowired
	private ReCaptcha reCaptcha;

	public boolean valid(ValidationParams params) {
		ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(params.getRemoteAddress(), params.getChallenge(), params.getResponse());
		return reCaptchaResponse != null && reCaptchaResponse.isValid();
	}

	public static class ValidationParams {
		private String remoteAddress;
		private String challenge;
		private String response;

		public ValidationParams(String remoteAddress, String challenge, String response) {
			this.remoteAddress = remoteAddress;
			this.challenge = challenge;
			this.response = response;
		}

		public String getRemoteAddress() {
			return remoteAddress;
		}

		public String getChallenge() {
			return challenge;
		}

		public String getResponse() {
			return response;
		}
	}
}
