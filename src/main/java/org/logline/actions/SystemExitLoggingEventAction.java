package org.logline.actions;

import org.logline.ILoggingEvent;
import org.logline.ILoggingEventAction;

/**
 * @author Yinon Sharifi
 */

public class SystemExitLoggingEventAction implements ILoggingEventAction {
	private static final long serialVersionUID = 1L;

	@Override
	public void act(ILoggingEvent loggingEvent) {
		System.exit(0);
	}

}
