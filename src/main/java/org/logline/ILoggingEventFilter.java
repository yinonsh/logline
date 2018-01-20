package org.logline;

/**
 * @author Yinon Sharifi
 */

public interface ILoggingEventFilter {
	boolean accept(ILoggingEvent event);
}
