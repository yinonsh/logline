package org.logline.filters;

import java.util.regex.Pattern;

import org.logline.ILoggingEvent;
import org.logline.ILoggingEventFilter;

/**
 * @author Yinon Sharifi
 */

public class PatternBasedLoggingEventFilter implements ILoggingEventFilter {
	private static final long serialVersionUID = 1L;
	private Pattern pattern;

	public PatternBasedLoggingEventFilter(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean accept(ILoggingEvent event) {
		return pattern.matcher(event.getFormattedMessage()).matches();
	}

}
