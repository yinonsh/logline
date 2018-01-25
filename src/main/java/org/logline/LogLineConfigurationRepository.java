package org.logline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.logline.LogLineConfiguration.ILoggingEventFilterWrapper;

/**
 * @author Yinon Sharifi
 */

public class LogLineConfigurationRepository {
	public static final String DEFAULT_CONF = "_DEFAULT_CONFIGURATION_";

	private static Map<String, LogLineConfiguration> configurations = new ConcurrentHashMap<String, LogLineConfiguration>();

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
		return new ArrayList<LogLineConfiguration>(configurations.values());
	}

	// syntactic sugar
	public static ILoggingEventFilterWrapper on(ILoggingEventFilter filter) {
		return getDefault().on(filter);
	}

}
