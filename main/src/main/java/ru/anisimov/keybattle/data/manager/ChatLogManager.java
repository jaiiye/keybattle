package ru.anisimov.keybattle.data.manager;

import org.springframework.stereotype.Component;
import ru.anisimov.keybattle.model.chat.ChatEvent;
import ru.anisimov.keybattle.model.chat.ChatMessage;
import ru.anisimov.keybattle.model.chat.UserFilter;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/23/14
 */
@Component
public class ChatLogManager {
	private static final int DEFAULT_SIZE = 3;

	private Queue<ChatEvent> chatLog = new ConcurrentLinkedQueue<>();

	private int size;

	public ChatLogManager() {
		this(DEFAULT_SIZE);
	}

	public ChatLogManager(int size) {
		this.size = size;
	}

	synchronized public void add(ChatEvent chatEvent) {
		chatLog.offer(chatEvent);
		if (chatLog.size() > size) {
			chatLog.poll();
		}
	}

	public List<ChatEvent> getLog(String recipient, UserFilter userFilter) {
		if (userFilter.getUsers() != null) {
			Set<String> filters = new HashSet<>(userFilter.getUsers());
			return chatLog.stream()
					.map(event -> {
								List<ChatMessage> messages = event.getMessages().stream()
										.filter(message -> (message.getRecipients().getUsers().size() == 0 || message.getRecipients().getUsers().contains(recipient))
												&& (filters.size() == 0 || filters.contains(message.getUserName()))).collect(Collectors.toList());
								return new ChatEvent(event.getEventtime(), event.getType(), new ArrayList<>(messages));
							})
					.filter(event -> event.getMessages().size() > 0)
					.collect(Collectors.toList());
		} else {
			return new ArrayList<>(chatLog);
		}
	}
}
