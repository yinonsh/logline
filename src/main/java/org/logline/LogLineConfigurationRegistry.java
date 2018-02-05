package org.logline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.logline.LogLineConfiguration.ILoggingEventFilterWrapper;
import org.logline.filters.ExactMessageLoggingEventFilter;
import org.logline.filters.PatternBasedLoggingEventFilter;
import org.logline.filters.StartsWithMessageLoggingEventFilter;

/**
 * @author Yinon Sharifi
 */

public class LogLineConfigurationRegistry implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_CONF = "_DEFAULT_CONFIGURATION_";

	private static Map<String, LogLineConfiguration> configurations = new ConcurrentHashMap<>();

	static {
		register(new LogLineConfiguration(DEFAULT_CONF));
	}

	public static void register(LogLineConfiguration configuration) {
		configurations.put(configuration.getName(), configuration);
	}

	public static void clear() {
		configurations.clear();
		register(new LogLineConfiguration(DEFAULT_CONF));
	}

	public static LogLineConfiguration unregister(String name) {
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
		return new ArrayList<>(configurations.values());
	}

	// syntactic sugar

	public static ILoggingEventFilterWrapper on(String text) {
		return getDefault().on(new ExactMessageLoggingEventFilter(text));
	}

	public static ILoggingEventFilterWrapper onStartWith(String text) {
		return getDefault().on(new StartsWithMessageLoggingEventFilter(text));
	}

	public static ILoggingEventFilterWrapper onPattern(String text) {
		return getDefault().on(new PatternBasedLoggingEventFilter(text));
	}

	public static ILoggingEventFilterWrapper on(ILoggingEventFilter filter) {
		return getDefault().on(filter);
	}

}
