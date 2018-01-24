package org.logline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.logline.actions.DelayLoggingEventAction;
import org.logline.actions.ThrowExceptionLoggingEventAction;

/**
 * @author Yinon Sharifi
 */

public class LogLineConfigurations {
	public static final String DEFAULT_CONF = "_DEFAULT_CONFIGURATION_";

	private static Map<String, LogLineConfiguration> configurations = new ConcurrentHashMap<String, LogLineConfiguration>();

	static {
		addConfiguration(new LogLineConfiguration(DEFAULT_CONF));
	}

	public static void addConfiguration(LogLineConfiguration configuration) {
		configurations.put(configuration.getName(), configuration);
	}

	public static void clear() {
		configurations.clear();
		addConfiguration(new LogLineConfiguration(DEFAULT_CONF));
	}

	public static LogLineConfiguration removeConfiguration(String name) {
		return configurations.remove(name);
	}

	public static LogLineConfiguration get(String name) {
		return configurations.get(name);
	}

	public static LogLineConfiguration getDefault() {
		LogLineConfiguration defaultConf = get(DEFAULT_CONF);

		if (defaultConf == null) {
			throw new IllegalStateException("Default configuration was removed");
		}
		return defaultConf;
	}

	public static Collection<LogLineConfiguration> getAllConfigurations() {
		return new ArrayList<LogLineConfiguration>(configurations.values());
	}

	// syntactic sugar
	public static ILoggingEventFilterWrapper on(ILoggingEventFilter filter) {
		return new ILoggingEventFilterWrapper(filter);
	}

	public static class ILoggingEventFilterWrapper {
		private final ILoggingEventFilter filter;

		public ILoggingEventFilterWrapper(ILoggingEventFilter filter) {
			this.filter = filter;
		}

		public ILoggingEventFilterWrapper run(ILoggingEventAction... actions) {
			LogLineConfigurations.getDefault().addActionsForFilter(filter, actions);
			return this;
		}

		public ILoggingEventFilterWrapper throwException(RuntimeException e) {
			LogLineConfigurations.getDefault().addActionsForFilter(filter, new ThrowExceptionLoggingEventAction(e));
			return this;
		}

		public ILoggingEventFilterWrapper delayMillis(long delayMs) {
			LogLineConfigurations.getDefault().addActionsForFilter(filter, new DelayLoggingEventAction(delayMs));
			return this;
		}
	}
}
