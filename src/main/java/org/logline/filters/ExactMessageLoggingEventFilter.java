package org.logline.filters;

import org.logline.ILoggingEvent;
import org.logline.ILoggingEventFilter;

/**
 * @author Yinon Sharifi
 */

public class ExactMessageLoggingEventFilter implements ILoggingEventFilter {
	private String message;

	public ExactMessageLoggingEventFilter(String message) {
		this.message = message;
	}

	@Override
	public boolean accept(ILoggingEvent event) {
		return message.equals(event.getFormattedMessage());
	}

}
