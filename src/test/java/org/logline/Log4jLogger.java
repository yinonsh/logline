package org.logline;

import org.apache.log4j.Logger;

/**
 * @author Yinon Sharifi
 */

public class Log4jLogger implements ILogger {

	private Logger logger;

	public Log4jLogger(Logger logger) {
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
