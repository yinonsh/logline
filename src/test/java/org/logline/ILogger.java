package org.logline;

public interface ILogger {
	public enum Level {
		TRACE,
		DEBUG,
		INFO,
		WARN,
		ERROR
	}

	void log(String message, Level level);

	default void trace(String message) {
		log(message, Level.TRACE);
	}

	default void debug(String message) {
		log(message, Level.DEBUG);
	}

	default void info(String message) {
		log(message, Level.INFO);
	}

	default void warn(String message) {
		log(message, Level.WARN);
	}

	default void error(String message) {
		log(message, Level.ERROR);
	}
}
