package org.logline;

/**
 * @author Yinon Sharifi
 */

public class LogLineAppender {

	private ThreadLocal<Boolean> threadLocalGuard = new ThreadLocal<Boolean>();

	public void doAppend(ILoggingEvent event) {
		// prevent re-entry.
		if (Boolean.TRUE.equals(threadLocalGuard.get())) {
			return;
		}

		try {
			threadLocalGuard.set(Boolean.TRUE);
			LoggingEventProcessor.process(event);
		} finally {
			threadLocalGuard.set(Boolean.FALSE);
		}
	}
}
