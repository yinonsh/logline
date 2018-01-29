package org.logline.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.logline.LogLineAppender;

/**
 * @author Yinon Sharifi
 */

public class Log4jLogLineAppender extends AppenderSkeleton {
	private LogLineAppender logLineAppender = new LogLineAppender();

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

	@Override
	protected void append(LoggingEvent event) {
		logLineAppender.doAppend(new Log4jLoggingEvent(event));
	}

}