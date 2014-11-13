package ru.anisimov.keybattle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import ru.anisimov.keybattle.websocket.interceptor.UserOnlineInterceptor;
import ru.anisimov.keybattle.data.manager.UsersOnlineManager;

import static ru.anisimov.keybattle.config.DestinationConfig.BATTLE_ROOM_ADDRESS;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/20/14
 */
@Configuration
@EnableWebSocketMessageBroker
@ComponentScan("ru.anisimov.keybattle.data.manager")
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
	private static final String DESTINATION_PREFIX = "/app";
	
	private UserOnlineInterceptor userOnlineInterceptor;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker(
				BATTLE_ROOM_ADDRESS + "/"
		);
		config.setApplicationDestinationPrefixes(DESTINATION_PREFIX);
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(BATTLE_ROOM_ADDRESS).withSockJS();
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.setInterceptors(userOnlineInterceptor());
	}

	@Bean
	public UserOnlineInterceptor userOnlineInterceptor() {
		userOnlineInterceptor = new UserOnlineInterceptor();
		return userOnlineInterceptor;
	}

	@Bean
	public UsersOnlineManager usersOnlineManager(SimpMessagingTemplate template) {
		UsersOnlineManager usersOnlineManager = new UsersOnlineManager();
		userOnlineInterceptor.setUsersOnlineManager(usersOnlineManager);
		return usersOnlineManager;
	}
}
