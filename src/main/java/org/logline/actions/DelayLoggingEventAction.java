package org.logline.actions;

import org.logline.ILoggingEvent;
import org.logline.ILoggingEventAction;

public class DelayLoggingEventAction implements ILoggingEventAction {
	private long delayMs;

	public DelayLoggingEventAction(long delayMs) {
		this.delayMs = delayMs;
	}

	@Override
	public void act(ILoggingEvent loggingEvent) {
		try {
			Thread.sleep(delayMs);
		} catch (InterruptedException e) {
		}
	}

}
