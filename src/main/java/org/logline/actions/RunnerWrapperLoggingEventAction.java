package org.logline.actions;

import org.logline.ILoggingEvent;
import org.logline.ILoggingEventAction;

/**
 * @author Yinon Sharifi
 */

public class RunnerWrapperLoggingEventAction implements ILoggingEventAction {
	private static final long serialVersionUID = 1L;

	private Runnable runnable;

	public RunnerWrapperLoggingEventAction(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void act(ILoggingEvent loggingEvent) {
		runnable.run();
	}

}
