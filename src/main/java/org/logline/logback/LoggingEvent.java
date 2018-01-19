package org.logline.logback;

import org.logline.ILoggingEvent;

public class LoggingEvent implements ILoggingEvent {

	ch.qos.logback.classic.spi.ILoggingEvent logbackLoggingEvent;

	public LoggingEvent(ch.qos.logback.classic.spi.ILoggingEvent logbackLoggingEvent) {
		this.logbackLoggingEvent = logbackLoggingEvent;
	}

	@Override
	public String getFormattedMessage() {
		return logbackLoggingEvent.getFormattedMessage();
	}

}
