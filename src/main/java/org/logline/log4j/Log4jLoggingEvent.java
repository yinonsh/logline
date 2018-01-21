package org.logline.log4j;

import org.logline.ILoggingEvent;

/**
 * @author Yinon Sharifi
 */

public class Log4jLoggingEvent implements ILoggingEvent {

	org.apache.log4j.spi.LoggingEvent log4jLoggingEvent;

	public Log4jLoggingEvent(org.apache.log4j.spi.LoggingEvent log4jLoggingEvent) {
		super();
		this.log4jLoggingEvent = log4jLoggingEvent;
	}

	@Override
	public String getFormattedMessage() {
		return log4jLoggingEvent.getRenderedMessage();
	}

}
