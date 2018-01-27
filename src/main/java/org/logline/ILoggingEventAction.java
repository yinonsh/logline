package org.logline;

import java.io.Serializable;

/**
 * @author Yinon Sharifi
 */

public interface ILoggingEventAction extends Serializable {
	public void act(ILoggingEvent loggingEvent);
}
