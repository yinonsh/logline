package org.logline.filters;

import org.logline.ILoggingEvent;
import org.logline.ILoggingEventFilter;

public class StartsWithMessageLoggingEventFilter implements ILoggingEventFilter {
	private String message;

	public StartsWithMessageLoggingEventFilter(String message) {
		this.message = message;
	}

	@Override
	public boolean accept(ILoggingEvent event) {
		return event.getFormattedMessage().startsWith(message);
	}
}
