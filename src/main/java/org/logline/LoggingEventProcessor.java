package org.logline;

import java.util.Collection;

/**
 * @author Yinon Sharifi
 */

public class LoggingEventProcessor {
	public static void process(ILoggingEvent event) {
		Collection<LogLineConfiguration> allConfigurations = LogLineConfigurations.getAllConfigurations();
		allConfigurations.forEach(x -> process(x, event));
	}

	private static void process(LogLineConfiguration configuration, ILoggingEvent event) {
		for (ILoggingEventFilter filter : configuration.getFilters()) {
			if (filter.accept(event)) {
				for (ILoggingEventAction action : configuration.getActions(filter)) {
					action.act(event);
				}
			}
		}
	}
}
