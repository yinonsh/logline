package org.logline.actions;

import org.logline.ILoggingEvent;
import org.logline.ILoggingEventAction;

/**
 * @author Yinon Sharifi
 */

public class NotifyEventLoggingEventAction implements ILoggingEventAction {
	private static final long serialVersionUID = 1L;
	private final Object event;

	public NotifyEventLoggingEventAction(Object event) {
		this.event = event;
	}

	@Override
	public void act(ILoggingEvent loggingEvent) {
		event.notifyAll();
	}

}
