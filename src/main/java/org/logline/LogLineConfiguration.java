package org.logline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.logline.actions.DelayLoggingEventAction;
import org.logline.actions.DumpStackTraceLoggingEventAction;
import org.logline.actions.NotifyEventLoggingEventAction;
import org.logline.actions.RunnerWrapperLoggingEventAction;
import org.logline.actions.SystemExitLoggingEventAction;
import org.logline.actions.ThrowExceptionLoggingEventAction;
import org.logline.actions.WaitForEventLoggingEventAction;
import org.logline.filters.ExactMessageLoggingEventFilter;
import org.logline.filters.PatternBasedLoggingEventFilter;
import org.logline.filters.StartsWithMessageLoggingEventFilter;
import org.logline.logger.ILogger;

/**
 * @author Yinon Sharifi
 */

public class LogLineConfiguration implements Serializable {
	private static final long serialVersionUID = 1L;

	private Map<ILoggingEventFilter, List<ILoggingEventAction>> filtersToActions;
	private final String name;
	private boolean isEnabled;

	public LogLineConfiguration(String name) {
		this(name, new ConcurrentHashMap<ILoggingEventFilter, List<ILoggingEventAction>>());
	}

	public LogLineConfiguration(String name, Map<ILoggingEventFilter, List<ILoggingEventAction>> filtersToActions) {
		this(name, filtersToActions, true);
	}

	public LogLineConfiguration(String name, Map<ILoggingEventFilter, List<ILoggingEventAction>> filtersToActions,
			boolean isEnabled) {
		this.name = name;
		this.filtersToActions = filtersToActions;
		this.isEnabled = isEnabled;

		LogLineConfigurationRegistry.register(this);
	}

	public void put(ILoggingEventFilter filter, ILoggingEventAction... actions) {
		put(filter, Arrays.asList(actions));
	}

	public void put(ILoggingEventFilter filter, List<ILoggingEventAction> actions) {
		List<ILoggingEventAction> existingActions = filtersToActions.get(filter);
		if (existingActions == null) {
			existingActions = new ArrayList<>();
			filtersToActions.put(filter, existingActions);
		}
		existingActions.addAll(actions);
	}

	public Collection<ILoggingEventFilter> getFilters() {
		return filtersToActions.keySet();
	}

	public List<ILoggingEventAction> getActions(ILoggingEventFilter filter) {
		return new ArrayList<>(filtersToActions.get(filter));
	}

	public void clear() {
		filtersToActions.clear();
	}

	public String getName() {
		return name;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	// syntactic sugar

	public ILoggingEventFilterWrapper on(ILoggingEventFilter filter) {
		return new ILoggingEventFilterWrapper(this, filter);
	}

	public ILoggingEventFilterWrapper on(String text) {
		return on(new ExactMessageLoggingEventFilter(text));
	}

	public ILoggingEventFilterWrapper onStartWith(String text) {
		return on(new StartsWithMessageLoggingEventFilter(text));
	}

	public ILoggingEventFilterWrapper onMatch(String text) {
		return on(new PatternBasedLoggingEventFilter(text));
	}

	public static class ILoggingEventFilterWrapper {
		private final ILoggingEventFilter filter;
		private final LogLineConfiguration configuration;

		public ILoggingEventFilterWrapper(LogLineConfiguration configuration, ILoggingEventFilter filter) {
			this.filter = filter;
			this.configuration = configuration;
		}

		public ILoggingEventFilterWrapper run(Runnable runnable) {
			configuration.put(filter, new RunnerWrapperLoggingEventAction(runnable));
			return this;
		}

		public ILoggingEventFilterWrapper run(ILoggingEventAction... actions) {
			configuration.put(filter, actions);
			return this;
		}

		public ILoggingEventFilterWrapper throwException(RuntimeException e) {
			configuration.put(filter, new ThrowExceptionLoggingEventAction(e));
			return this;
		}

		public ILoggingEventFilterWrapper delayMillis(long delayMs) {
			configuration.put(filter, new DelayLoggingEventAction(delayMs));
			return this;
		}

		public ILoggingEventFilterWrapper threadDump(ILogger logger) {
			configuration.put(filter, new DumpStackTraceLoggingEventAction(logger));
			return this;
		}

		public ILoggingEventFilterWrapper exit(int exitCode) {
			configuration.put(filter, new SystemExitLoggingEventAction(exitCode));
			return this;
		}

		public ILoggingEventFilterWrapper waitFor(Object event) {
			configuration.put(filter, new WaitForEventLoggingEventAction(event));
			return this;
		}

		public ILoggingEventFilterWrapper notifyOf(Object event) {
			configuration.put(filter, new NotifyEventLoggingEventAction(event));
			return this;
		}
	}
}
