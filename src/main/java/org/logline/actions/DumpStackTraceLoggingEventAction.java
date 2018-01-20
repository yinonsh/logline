package org.logline.actions;

import org.logline.ILoggingEvent;
import org.logline.ILoggingEventAction;
import org.logline.utils.ThreadDumper;

/**
 * @author Yinon Sharifi
 */

public class DumpStackTraceLoggingEventAction implements ILoggingEventAction {

	@Override
	public void act(ILoggingEvent loggingEvent) {
		ThreadDumper.dumpAllThreadStackTraces();
	}

}
