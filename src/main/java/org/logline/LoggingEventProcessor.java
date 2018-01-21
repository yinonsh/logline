package org.logline;

public class LoggingEventProcessor {
	public static void process(ILoggingEvent event) {

		for (ILoggingEventFilter filter : LogLineConfiguration.getFilters()) {
			if (filter.accept(event)) {
				for (ILoggingEventAction action : LogLineConfiguration.getActions(filter)) {
					action.act(event);
				}
			}
		}
	}
}
