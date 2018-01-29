package org.logline.log4j;

import org.apache.logging.log4j.core.LogEvent;
import org.logline.ILoggingEvent;

/**
 * @author Yinon Sharifi
 */

public class Log4jLoggingEvent implements ILoggingEvent {

	LogEvent logEvent;

	public Log4jLoggingEvent(LogEvent logEvent) {
		super();
		this.logEvent = logEvent;
	}

	@Override
	public String getFormattedMessage() {
		return logEvent.getMessage().getFormattedMessage();
	}

}
