package org.logline.actions;

import org.logline.ILoggingEvent;
import org.logline.ILoggingEventAction;

/**
 * @author Yinon Sharifi
 */

public class ThrowExceptionLoggingEventAction implements ILoggingEventAction {
	private static final long serialVersionUID = 1L;
	private RuntimeException exception;

	public ThrowExceptionLoggingEventAction(RuntimeException exception) {
		this.exception = exception;
	}

	@Override
	public void act(ILoggingEvent loggingEvent) {
		throw exception;
	}

}
