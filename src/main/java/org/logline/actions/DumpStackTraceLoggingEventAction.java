package org.logline.actions;

import org.logline.ILoggingEvent;
import org.logline.ILoggingEventAction;
import org.logline.logger.ILogger;
import org.logline.utils.ThreadDumper;

/**
 * @author Yinon Sharifi
 */

public class DumpStackTraceLoggingEventAction implements ILoggingEventAction {
	private static final long serialVersionUID = 1L;

	private ILogger logger;

	public DumpStackTraceLoggingEventAction(ILogger logger) {
		this.logger = logger;
	}

	@Override
	public void act(ILoggingEvent loggingEvent) {
		ThreadDumper.dumpAllThreadStackTraces(logger);
	}

}
