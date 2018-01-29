package org.logline.actions;

import org.logline.ILoggingEvent;
import org.logline.ILoggingEventAction;

/**
 * @author Yinon Sharifi
 */

public class WaitForEventLoggingEventAction implements ILoggingEventAction {
	private static final long serialVersionUID = 1L;
	private final Object event;

	public WaitForEventLoggingEventAction(Object event) {
		this.event = event;
	}

	@Override
	public void act(ILoggingEvent loggingEvent) {
		synchronized (event) {
			try {
				event.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
