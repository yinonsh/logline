package org.logline.actions;

import org.logline.ILoggingEvent;
import org.logline.ILoggingEventAction;

public class SystemExitLoggingEventAction implements ILoggingEventAction {

	@Override
	public void act(ILoggingEvent loggingEvent) {
		System.exit(0);
	}

}
