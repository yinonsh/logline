package org.logline;

import org.logline.logger.ILogger;

public abstract class BaseLogger implements ILogger {
	@Override
	public void trace(String message) {
		log(message, Level.TRACE);
	}

	@Override
	public void debug(String message) {
		log(message, Level.DEBUG);
	}

	@Override
	public void info(String message) {
		log(message, Level.INFO);
	}

	@Override
	public void warn(String message) {
		log(message, Level.WARN);
	}

	@Override
	public void error(String message) {
		log(message, Level.ERROR);
	}
}
