package org.logline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.logline.actions.DelayLoggingEventAction;
import org.logline.actions.ThrowExceptionLoggingEventAction;

/**
 * @author Yinon Sharifi
 */

public class LogLineConfiguration {
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

		LogLineConfigurations.addConfiguration(this);
	}

	public void addActionsForFilter(ILoggingEventFilter filter, ILoggingEventAction... actions) {
		addActionsForFilter(filter, Arrays.asList(actions));
	}

	public void addActionsForFilter(ILoggingEventFilter filter, List<ILoggingEventAction> actions) {
		List<ILoggingEventAction> list = filtersToActions.get(filter);
		if (list == null) {
			list = new ArrayList<ILoggingEventAction>();
		}

		list.addAll(actions);
		filtersToActions.put(filter, list);
	}

	public void setActionsForFilter(ILoggingEventFilter filter, List<ILoggingEventAction> actions) {
		List<ILoggingEventAction> list = filtersToActions.get(filter);
		if (list == null) {
			list = new ArrayList<ILoggingEventAction>();
		} else {
			list.clear();
		}
		addActionsForFilter(filter, actions);
	}

	public Collection<ILoggingEventFilter> getFilters() {
		return filtersToActions.keySet();
	}

	public List<ILoggingEventAction> getActions(ILoggingEventFilter filter) {
		return new ArrayList<ILoggingEventAction>(filtersToActions.get(filter));
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

	public static class ILoggingEventFilterWrapper {
		private final ILoggingEventFilter filter;
		private final LogLineConfiguration configuration;

		public ILoggingEventFilterWrapper(LogLineConfiguration configuration, ILoggingEventFilter filter) {
			this.filter = filter;
			this.configuration = configuration;
		}

		public ILoggingEventFilterWrapper run(ILoggingEventAction... actions) {
			configuration.addActionsForFilter(filter, actions);
			return this;
		}

		public ILoggingEventFilterWrapper throwException(RuntimeException e) {
			configuration.addActionsForFilter(filter, new ThrowExceptionLoggingEventAction(e));
			return this;
		}

		public ILoggingEventFilterWrapper delayMillis(long delayMs) {
			configuration.addActionsForFilter(filter, new DelayLoggingEventAction(delayMs));
			return this;
		}
	}
}
