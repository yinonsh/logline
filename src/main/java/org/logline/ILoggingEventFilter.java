package org.logline;

public interface ILoggingEventFilter {
	boolean accept(ILoggingEvent event);
}
