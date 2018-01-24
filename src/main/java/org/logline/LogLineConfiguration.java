package org.logline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yinon Sharifi
 */

public class LogLineConfiguration {
	private Map<ILoggingEventFilter, List<ILoggingEventAction>> filtersToActions;
	private final String name;
	private boolean isEnabled;

	public LogLineConfiguration(String name) {
		this(name, new ConcurrentHashMap<>());
	}

	public LogLineConfiguration(String name, Map<ILoggingEventFilter, List<ILoggingEventAction>> filtersToActions) {
		this.name = name;
		this.filtersToActions = filtersToActions;
		isEnabled = true;
	}

	public void addActionsForFilter(ILoggingEventFilter filter, ILoggingEventAction... actions) {
		addActionsForFilter(filter, Arrays.asList(actions));
	}

	public void addActionsForFilter(ILoggingEventFilter filter, List<ILoggingEventAction> actions) {
		List<ILoggingEventAction> list = filtersToActions.getOrDefault(filter, new ArrayList<>());
		list.addAll(actions);
		filtersToActions.put(filter, list);
	}

	public void setActionsForFilter(ILoggingEventFilter filter, List<ILoggingEventAction> actions) {
		List<ILoggingEventAction> list = filtersToActions.getOrDefault(filter, new ArrayList<>());
		list.clear();
		addActionsForFilter(filter, actions);
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

}
