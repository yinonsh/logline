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

	@Override
	protected void append(LoggingEvent event) {
		LoggingEventProcessor.process(new Log4jLoggingEvent(event));
	}

}