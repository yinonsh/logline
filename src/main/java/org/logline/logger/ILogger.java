package org.logline.logger;

/**
 * @author Yinon Sharifi
 */

public interface ILogger {
	public enum Level {
		TRACE,
		DEBUG,
		INFO,
		WARN,
		ERROR
	}

	void log(String message, Level level);

	void trace(String message);

	void debug(String message);

	void info(String message);

	void warn(String message);

	void error(String message);
}
