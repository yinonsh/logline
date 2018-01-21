package org.logline.logback;

import org.logline.ILoggingEvent;

/**
 * @author Yinon Sharifi
 */

public class LogbackLoggingEvent implements ILoggingEvent {

	ch.qos.logback.classic.spi.ILoggingEvent logbackLoggingEvent;

	public LogbackLoggingEvent(ch.qos.logback.classic.spi.ILoggingEvent logbackLoggingEvent) {
		this.logbackLoggingEvent = logbackLoggingEvent;
	}

	@Override
	public String getFormattedMessage() {
		return logbackLoggingEvent.getFormattedMessage();
	}

}
