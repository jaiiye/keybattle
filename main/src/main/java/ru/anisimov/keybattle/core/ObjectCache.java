package ru.anisimov.keybattle.core;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         11/7/14
 */
public class ObjectCache<T> {
	private static final long DEFAULT_EXPIRATION_TIME = 1000 * 10; // 10 sec 
	
	private ObjectBuilder<T> builder;	
	private T object;
	private long lastBuildTime;
	private long expirationTime;	

	public ObjectCache(ObjectBuilder<T> builder) {
		this(builder, DEFAULT_EXPIRATION_TIME);
	}

	public ObjectCache(ObjectBuilder<T> builder, long expirationTime) {
		this.builder = builder;
		this.expirationTime = expirationTime;
		buildObject();
	}
	
	public void buildObject() {
		object = builder.build();
		lastBuildTime = System.currentTimeMillis();
	}
	
	public T get() {
		if (System.currentTimeMillis() - lastBuildTime > expirationTime) {
			buildObject();
		}
		return object;
	}
}
