package org.logline.actions;

import org.logline.ILoggingEvent;
import org.logline.ILoggingEventAction;

/**
 * @author Yinon Sharifi
 */

public class SystemExitLoggingEventAction implements ILoggingEventAction {
	private static final long serialVersionUID = 1L;

	private int exitCode;

	public SystemExitLoggingEventAction() {
		exitCode = 0;
	}

	public SystemExitLoggingEventAction(int exitCode) {
		this.exitCode = exitCode;
	}

	@Override
	public void act(ILoggingEvent loggingEvent) {
		System.exit(exitCode);
	}

}
