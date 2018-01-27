package org.logline;

import java.io.Serializable;

/**
 * @author Yinon Sharifi
 */

public interface ILoggingEventFilter extends Serializable {
	boolean accept(ILoggingEvent event);
}
