package org.logline;

import ch.qos.logback.classic.Logger;

/**
 * @author Yinon Sharifi
 */

public class LogbackLogger implements ILogger {

	private Logger logger;

	public LogbackLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void log(String message, Level level) {
		switch (level) {
		case TRACE:
			logger.trace(message);
			break;
		case DEBUG:
			logger.debug(message);
			break;
		case INFO:
			logger.info(message);
			break;
		case WARN:
			logger.warn(message);
			break;
		case ERROR:
			logger.error(message);
			break;
		}
	}

}