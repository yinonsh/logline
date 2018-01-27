package org.logline.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.logline.LoggingEventProcessor;

/**
 * @author Yinon Sharifi
 */

public class Log4jLogLineAppender extends AppenderSkeleton {

	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	/**
	 * The guard prevents an appender from repeatedly calling its own doAppend
	 * method.
	 */
	private boolean guard = false;

	@Override
	protected void append(LoggingEvent event) {
		// prevent re-entry.
		if (guard) {
			return;
		}

		try {
			guard = true;
			LoggingEventProcessor.process(new Log4jLoggingEvent(event));
		} finally {
			guard = false;
		}

	}

}