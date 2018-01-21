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
	private static Map<ILoggingEventFilter, List<ILoggingEventAction>> filtersToActions = new ConcurrentHashMap<>();

	public static void addActionsForFilter(ILoggingEventFilter filter, ILoggingEventAction... actions) {
		addActionsForFilter(filter, Arrays.asList(actions));
	}

	public static void addActionsForFilter(ILoggingEventFilter filter, List<ILoggingEventAction> actions) {
		List<ILoggingEventAction> list = filtersToActions.getOrDefault(filter, new ArrayList<>());
		list.addAll(actions);
		filtersToActions.put(filter, list);
	}

	public static void setActionsForFilter(ILoggingEventFilter filter, List<ILoggingEventAction> actions) {
		List<ILoggingEventAction> list = filtersToActions.getOrDefault(filter, new ArrayList<>());
		list.clear();
		addActionsForFilter(filter, actions);
	}

	public static Collection<ILoggingEventFilter> getFilters() {
		return filtersToActions.keySet();
	}

	public static List<ILoggingEventAction> getActions(ILoggingEventFilter filter) {
		return new ArrayList<>(filtersToActions.get(filter));
	}

	public static void clear() {
		filtersToActions.clear();
	}

	public static ILoggingEventFilterWrapper on(ILoggingEventFilter filter) {
		return new ILoggingEventFilterWrapper(filter);
	}

	public static class ILoggingEventFilterWrapper {
		private final ILoggingEventFilter filter;

		public ILoggingEventFilterWrapper(ILoggingEventFilter filter) {
			this.filter = filter;
		}

		public ILoggingEventFilterWrapper run(ILoggingEventAction... actions) {
			LogLineConfiguration.addActionsForFilter(filter, actions);
			return this;
		}

		public ILoggingEventFilterWrapper throwException(RuntimeException e) {
			LogLineConfiguration.addActionsForFilter(filter, new ThrowExceptionLoggingEventAction(e));
			return this;
		}

		public ILoggingEventFilterWrapper delayMillis(long delayMs) {
			LogLineConfiguration.addActionsForFilter(filter, new DelayLoggingEventAction(delayMs));
			return this;
		}
	}
}
